/*******************************************************************************
 *     Cloud Foundry
 *     Copyright (c) [2009-2016] Pivotal Software, Inc. All Rights Reserved.
 *
 *     This product is licensed to you under the Apache License, Version 2.0 (the "License").
 *     You may not use this product except in compliance with the License.
 *
 *     This product includes a number of subcomponents with
 *     separate copyright notices and license terms. Your use of these
 *     subcomponents is subject to the terms and conditions of the
 *     subcomponent's license, as noted in the LICENSE file.
 *******************************************************************************/
package org.cloudfoundry.identity.uaa.account;

import org.cloudfoundry.identity.uaa.authentication.UaaPrincipal;
import org.cloudfoundry.identity.uaa.approval.ApprovalsService;
import org.cloudfoundry.identity.uaa.approval.DescribedApproval;
import org.cloudfoundry.identity.uaa.oauth.client.ClientConstants;
import org.cloudfoundry.identity.uaa.constants.OriginKeys;
import org.cloudfoundry.identity.uaa.approval.Approval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProfileController {

    private final ApprovalsService approvalsService;
    private final ClientDetailsService clientDetailsService;

    @Autowired
    public ProfileController(ApprovalsService approvalsService,
                             ClientDetailsService clientDetailsService) {
        this.approvalsService = approvalsService;
        this.clientDetailsService = clientDetailsService;
    }

    /**
     * Display the current user's approvals
     */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String get(Authentication authentication, Model model) {
        Map<String, List<DescribedApproval>> approvals = approvalsService.getCurrentApprovalsByClientId();
        Map<String, String> clientNames = getClientNames(approvals);
        model.addAttribute("clientnames", clientNames);
        model.addAttribute("approvals", approvals);
        model.addAttribute("isUaaManagedUser", isUaaManagedUser(authentication));
        return "approvals";
    }

    protected Map<String, String> getClientNames(Map<String, List<DescribedApproval>> approvals) {
        Map<String, String> clientNames = new LinkedHashMap<>();
        for (String clientId : approvals.keySet()) {
            ClientDetails details = clientDetailsService.loadClientByClientId(clientId);
            String name = details.getClientId();
            if (details.getAdditionalInformation()!=null && details.getAdditionalInformation().get(ClientConstants.CLIENT_NAME)!=null) {
                name = (String)details.getAdditionalInformation().get(ClientConstants.CLIENT_NAME);
            }
            clientNames.put(clientId, name);
        }
        return clientNames;
    }

    /**
     * Handle form post for revoking chosen approvals
     */
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String post(@RequestParam(required = false) Collection<String> checkedScopes,
                       @RequestParam(required = false) String update,
                       @RequestParam(required = false) String delete,
                       @RequestParam(required = false) String clientId) {

        if (null != update) {
            Map<String, List<DescribedApproval>> approvalsByClientId = approvalsService.getCurrentApprovalsByClientId();

            List<DescribedApproval> allApprovals = new ArrayList<>();
            for (List<DescribedApproval> clientApprovals : approvalsByClientId.values()) {
                allApprovals.addAll(clientApprovals);
            }

            for (Approval approval : allApprovals) {
                String namespacedScope = approval.getClientId() + "-" + approval.getScope();
                if (checkedScopes != null && checkedScopes.contains(namespacedScope)) {
                    approval.setStatus(Approval.ApprovalStatus.APPROVED);
                } else {
                    approval.setStatus(Approval.ApprovalStatus.DENIED);
                }
            }

            approvalsService.updateApprovals(allApprovals);
        }
        else if (null != delete) {
            approvalsService.deleteApprovalsForClient(clientId);
        }

        return "redirect:profile";
    }

    private boolean isUaaManagedUser(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UaaPrincipal) {
            UaaPrincipal principal = (UaaPrincipal) authentication.getPrincipal();
            return OriginKeys.UAA.equals(principal.getOrigin());
        }
        return false;
    }
}

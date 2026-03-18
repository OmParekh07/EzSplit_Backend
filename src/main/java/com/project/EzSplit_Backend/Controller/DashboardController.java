package com.project.EzSplit_Backend.Controller;



import com.project.EzSplit_Backend.Dto.DashboardDto;

import com.project.EzSplit_Backend.Dto.UpdateDetailDto;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardDto getDashboard(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return dashboardService.getDashboard(user.getId());
    }

    @PostMapping
    public String updateDashboard(@RequestBody UpdateDetailDto dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        dashboardService.updateDashboard(user.getId(), dto);
        return "UPDATED";
    }
}
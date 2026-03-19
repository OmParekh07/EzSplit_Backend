    package com.project.EzSplit_Backend.Controller;

    import com.project.EzSplit_Backend.Dto.*;
    import com.project.EzSplit_Backend.Entity.Group;
    import com.project.EzSplit_Backend.Entity.User;
    import com.project.EzSplit_Backend.Service.ExpenseService;
    import com.project.EzSplit_Backend.Service.GroupService;
    import com.project.EzSplit_Backend.Service.SettlementService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.Authentication;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    import static org.springframework.http.ResponseEntity.ok;


    @RestController
    @RequestMapping("/groups")
    @RequiredArgsConstructor
    public class GroupController {

        private final GroupService groupService;
    private final SettlementService settlementService;
    private final ExpenseService expenseService;
        @PostMapping
        public ResponseEntity<CreateGroupResponseDto> createGroup(@RequestBody CreateGroupRequestDto request,
                                                                  Authentication authentication){

            User user = (User) authentication.getPrincipal();

            return ResponseEntity.ok(groupService.createGroup(request, user.getId()));
        }

        @GetMapping
        public List<GroupSummaryDto> getGroups(Authentication authentication){

            User user = (User) authentication.getPrincipal();
            System.out.println("User:"+user.getId());
            return groupService.getGroups(user.getId());
        }

        @PostMapping("/join/{inviteCode}")
        public JoinGroupResponseDto joinGroup(@PathVariable String inviteCode,
                                              Authentication authentication){

            User user = (User) authentication.getPrincipal();

            return groupService.joinGroup(inviteCode, user.getId());
        }

        @GetMapping("/inviteCode/{id}")
        public String inviteLink(@PathVariable Long groupId,Authentication authentication){
            User user = (User) authentication.getPrincipal();
            return groupService.getInviteCode(groupId);
        }

        @GetMapping("/{groupId}")
        public GroupDetailDto getGroupDetails(@PathVariable Long groupId){
            return groupService.getGroupDetails(groupId);
        }

        @GetMapping("/{groupId}/settlements")
        public List<SettlementListDto> getSettlements(@PathVariable("groupId") Long groupId){
            return settlementService.getGroupSettlements(groupId);
        }
        @GetMapping("/{groupId}/expenses")
        public List<ExpenseResponseDto> getGroupExpenses(@PathVariable Long groupId){

            return expenseService.getGroupExpenses(groupId);
        }

        @DeleteMapping("/{groupId}")
        public String deleteGroup(@PathVariable Long groupId, Authentication authentication) {
            User user = (User) authentication.getPrincipal();
            return groupService.deleteGroup(groupId, user.getId());
        }



    }
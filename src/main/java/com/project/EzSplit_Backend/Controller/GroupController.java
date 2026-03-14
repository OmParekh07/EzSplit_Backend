    package com.project.EzSplit_Backend.Controller;

    import com.project.EzSplit_Backend.Dto.CreateGroupRequestDto;
    import com.project.EzSplit_Backend.Dto.CreateGroupResponseDto;
    import com.project.EzSplit_Backend.Dto.GroupResponseDto;
    import com.project.EzSplit_Backend.Dto.SettlementTransactionDto;
    import com.project.EzSplit_Backend.Entity.Expense;
    import com.project.EzSplit_Backend.Entity.ExpenseSplit;
    import com.project.EzSplit_Backend.Entity.Group;
    import com.project.EzSplit_Backend.Entity.User;
    import com.project.EzSplit_Backend.Repository.ExpenseRepository;
    import com.project.EzSplit_Backend.Repository.ExpenseSplitRepository;
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
        private final ExpenseRepository expenseRepository;
        private final ExpenseSplitRepository expenseSplitRepository;
        private final SettlementService settlementService;

        @PostMapping
        public ResponseEntity<CreateGroupResponseDto> createGroup(@RequestBody CreateGroupRequestDto request,
                                                                  Authentication authentication){

            User user = (User) authentication.getPrincipal();

            return ResponseEntity.ok(groupService.createGroup(request, user.getId()));
        }

        @GetMapping
        public List<GroupResponseDto> getGroups(Authentication authentication){

            User user = (User) authentication.getPrincipal();
            System.out.println("User:"+user.getId());
            return groupService.getGroups(user.getId());
        }

        @PostMapping("/join/{inviteCode}")
        public String joinGroup(@PathVariable String inviteCode,
                                Authentication authentication){

            User user = (User) authentication.getPrincipal();

            return groupService.joinGroup(inviteCode, user.getId());
        }

        @GetMapping("/inviteCode/{id}")
        public String inviteLink(@PathVariable Long groupId,Authentication authentication){
            User user = (User) authentication.getPrincipal();
            return groupService.getInviteCode(groupId);
        }

        @GetMapping("/groups/{groupId}/settle")
        public List<SettlementTransactionDto> settleGroup(
                @PathVariable Long groupId
        ) {

            List<Expense> expenses =
                    expenseRepository.findByGroupId(groupId);

            List<ExpenseSplit> splits =
                    expenseSplitRepository.findByGroupId(groupId);

            return settlementService.generateSettlements(expenses, splits);
        }
    }
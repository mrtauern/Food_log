package com.base.site.services;

import com.base.site.models.Exercise;
import com.base.site.models.Users;
import com.base.site.repositories.ExerciseRepository;
import com.base.site.repositories.UsersRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.verify;

class UsersServiceTest {


    @InjectMocks
    private UsersService usersService;
    @Mock
    private UsersRepo usersRepo;

    @Test
    public void shouldSaveUserSuccecfully(){
        //final Users users = new Users("hasan", "haidari","hh@cph.dk","123456", null,null,31,null);
        final Users users = new Users(1l,"hasan", "haidari","hh@cph.dk","123456",182);

        given(usersRepo.findById(users.getId())).willReturn(Optional.empty());
        given(usersRepo.save(users)).willAnswer(invocation -> invocation.getArguments());

        Users savedUser = usersService.save(users);

        assertThat(savedUser).isNotNull();

        verify(usersRepo).save(any(Users.class));
    }

    @Test
    public void updateUser(){
        final Users users = new Users(1l,"aa", "aa","aa","aa", 182);

        given(usersRepo.save(users)).willReturn(users);
        final Users expected = usersService.findById(users.getId());

        assertThat(expected).isNotNull();

        verify(usersRepo).save(any(Users.class));
    }

}
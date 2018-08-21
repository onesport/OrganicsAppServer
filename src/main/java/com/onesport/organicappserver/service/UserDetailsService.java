package com.onesport.organicappserver.service;

import com.onesport.organicappserver.entity.UserDetailsEntity;
import com.onesport.organicappserver.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {
    @Autowired
    private UserDetailsRepository _userDetailsRepository;

    public Integer SaveUserDetails(UserDetailsEntity userDetails){
        UserDetailsEntity _userDetails =  _userDetailsRepository.save(userDetails);

        if(_userDetails !=null){
            return 1;
        }
        return 0;
    }

    public Boolean FindByEmail(String email){
        UserDetailsEntity _userDetails =  _userDetailsRepository.findByEmail(email);

        if(_userDetails !=null){
            return true;
        }
        return false;
    }
}
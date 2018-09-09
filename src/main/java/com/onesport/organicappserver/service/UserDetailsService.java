package com.onesport.organicappserver.service;

import com.onesport.organicappserver.entity.UserDetailsEntity;
import com.onesport.organicappserver.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsService {
    @Autowired
    private UserDetailsRepository _userDetailsRepository;

    public UserDetailsEntity saveUserDetails(UserDetailsEntity userDetails){
        return _userDetailsRepository.save(userDetails);
    }

    public Boolean FindByEmail(String email){
        List<UserDetailsEntity> _userDetails =  _userDetailsRepository.findByEmail(email);

        if(_userDetails.size() <=0){
            return true;
        }
        return false;
    }

    public Optional<UserDetailsEntity> findbyUserId(Integer userid){
        return  _userDetailsRepository.findById(userid);
    }
}
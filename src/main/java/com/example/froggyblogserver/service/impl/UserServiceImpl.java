package com.example.froggyblogserver.service.impl;

import com.example.froggyblogserver.dto.ChartUserDto;
import com.example.froggyblogserver.dto.UserDto;
import com.example.froggyblogserver.dto.request.UserSearchRequest;
import com.example.froggyblogserver.entity.*;
import com.example.froggyblogserver.exception.CheckedException;
import com.example.froggyblogserver.exception.UncheckedException;
import com.example.froggyblogserver.mapper.UserMapper;
import com.example.froggyblogserver.repository.*;
import com.example.froggyblogserver.response.PageResponse;
import com.example.froggyblogserver.service.CurrentUserService;
import com.example.froggyblogserver.utils.SortHelper;
import com.example.froggyblogserver.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.froggyblogserver.common.CONSTANTS;
import com.example.froggyblogserver.common.MESSAGE;
import com.example.froggyblogserver.exception.ValidateException;
import com.example.froggyblogserver.response.BaseResponse;
import com.example.froggyblogserver.service.UserService;

import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo repo;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserPostRepo userPostRepo;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private AccountRoleRepo accountRoleRepo;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private LikeRepo likeRepo;
    @Autowired
    private CommentRepo commentRepo;

    @Override
    public BaseResponse findById(String id) {
        var found = repo.findById(id).orElse(null);
        if (found == null)
            throw new ValidateException(MESSAGE.VALIDATE.ID_INVALID);
        return new BaseResponse(userMapper.entityToProfile(found));

    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse saveOrUpdate(UserDto req) {
//        if (!StringHelper.isNullOrEmpty(req.getEmail())) {
//            var checkEmail = accountRepo.findByEmail(req.getEmail());
//            if (checkEmail != null)
//                throw new ValidateInputException(CONSTANTS.PROPERTIES.EMAIL, MESSAGE.VALIDATE.EMAIL_ALREADY_EXIST, req.getEmail());
//            var user = currentUserService.getInfo();
//            if (user.getProvider().equals(CONSTANTS.PROVIDER.SYSTEM)) {
//                var account = accountRepo.findByEmail(user.getEmail());
//                account.setEmail(req.getEmail());
//                account.setUpdateId(user.getId());
//                accountRepo.save(account);
//            }
//        }
//        return new BaseResponse(repo.save(req).getId());
        //COMING SOON
        return null;
    }

    @Override
    public BaseResponse search(UserSearchRequest request, String column, String orderBy) {
        var page = PageRequest.of(request.getPageNumber() - 1, request.getPageSize());
        if(!StringHelper.isNullOrEmpty(column) && !StringHelper.isNullOrEmpty(orderBy))
            page = SortHelper.sort(page,orderBy,column);
        else page = SortHelper.sort(page,CONSTANTS.SORT.DESC,"createDate");
        var exec = repo.search(request, page);
        return new BaseResponse(PageResponse.builder()
                .data(exec.getContent().stream().map(userMapper::entityToProfile).collect(Collectors.toList()))
                .pageNumber(request.getPageNumber())
                .pageSize(request.getPageSize())
                .totalPage(exec.getTotalPages())
                .totalRecord(exec.getTotalElements())
                .build());
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse deleteById(String id) {
        Optional<UserEntity> found = repo.findById(id);
        if (!found.isPresent())
            throw new ValidateException(MESSAGE.VALIDATE.ID_INVALID);
        found.get().setDelete(CONSTANTS.BOOLEAN.TRUE);
        repo.save(found.get());
        return new BaseResponse(id);
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse savePost(String postId) {
        var info = currentUserService.getInfo();
        var favorite = UserPostEntity.builder().userId(info.getId()).postId(postId).build();
        var checkExist = userPostRepo.findByUserIdAndPostId(info.getId(),postId);
        if(checkExist.isPresent()) {
            userPostRepo.deleteById(checkExist.get().getId());
            return new BaseResponse(200,MESSAGE.RESPONSE.UN_SAVE_SUCCESS);
        }
        favorite.setDelete(CONSTANTS.BOOLEAN.FALSE);
        var save = userPostRepo.save(favorite);
        return new BaseResponse(200,MESSAGE.RESPONSE.SAVE_SUCCESS,save.getId());
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public void OAuthLogin(String name, String email) {
        var checkExist = repo.findByEmailanAndProvider(email, null);
        if (checkExist.isEmpty()) {
            var newUser = UserEntity.builder().fullName(name).email(email).provider(CONSTANTS.PROVIDER.GOOGLE).build();
            var execNewUser = repo.save(newUser);
            var newAccount = AccountEntity.builder().userId(execNewUser.getId()).email(email).password(passwordEncoder.encode(UUID.randomUUID().toString())).build();
            var saveNewAccount = accountRepo.save(newAccount);
            var roleUser = roleRepo.findByCode(CONSTANTS.ROLE.USER);
            var exec = accountRoleRepo.save(AccountsRolesEntity.builder().accountId(saveNewAccount.getId()).roleId(roleUser.get().getId()).build());
        }
    }

    @Override
    public UserDto findByEmail(String email) {
        var found = repo.findByEmailanAndProvider(email, null).orElse(null);
        if (found == null) return null;
        return userMapper.entityToDto(found);
    }

    @Override
    public BaseResponse getProfile() {
        var info = currentUserService.getInfo();
        return new BaseResponse(userMapper.entityToDto(info));
    }

    @Override
    @Transactional(rollbackOn = {UncheckedException.class, CheckedException.class})
    public BaseResponse updateProfile(UserDto dto) {
        if(StringHelper.isNullOrEmpty(dto.getId()))
            throw new ValidateException(MESSAGE.VALIDATE.ID_INVALID);
        var found = repo.findById(dto.getId());
        if (found.isEmpty())
            throw new ValidateException(MESSAGE.VALIDATE.ID_INVALID);
        var convertToEntity = userMapper.dtoToEntity(dto);
        convertToEntity.setEmail(found.get().getEmail());
        var update = repo.save(convertToEntity);
        var dtoRes = userMapper.entityToDto(update);
         var account = accountRepo.findByEmail(update.getEmail());
        dtoRes.setRoles(account.getRoles().parallelStream().map(RoleEntity::getName).collect(Collectors.toList()));
        return new BaseResponse(dtoRes);
    }

    @Override
    public BaseResponse searchAdmin(UserSearchRequest request, String column, String orderBy) {
        var page = PageRequest.of(request.getPageNumber() - 1, request.getPageSize());
        if(!StringHelper.isNullOrEmpty(column) && !StringHelper.isNullOrEmpty(orderBy))
            page = SortHelper.sort(page,orderBy,column);
        else page = SortHelper.sort(page,CONSTANTS.SORT.DESC,"createDate");
        var exec = repo.search(request, page);
        return new BaseResponse(PageResponse.builder()
                .data(exec.getContent().stream().map(user ->{
                    var dto = userMapper.entityToProfileAdmin(user);
                    var account= accountRepo.findByEmail(user.getEmail());
                    dto.setRole(account.getRoles());
                    return dto;
                }).collect(Collectors.toList()))
                .pageNumber(request.getPageNumber())
                .pageSize(request.getPageSize())
                .totalPage(exec.getTotalPages())
                .totalRecord(exec.getTotalElements())
                .build());
    }

    @Override
    public BaseResponse chartUser(Integer period) {
        var info = currentUserService.getInfo();
        var allPost = postRepo.getAllPostByAuthor(info.getId());
        var now = LocalDateTime.now();
        var pastTime = now.minusDays(period - 1);
        List<ChartUserDto> chartUserDto = new ArrayList<>();
        for (var temp = pastTime; !temp.isAfter(now); temp = temp.plusDays(1)){
            var minTempDate = temp.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toLocalDateTime();
            var maxTempDate = temp.toLocalDate().atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toLocalDateTime();
            var countsComment  = 0L;
            var countsLike = 0L;
            var countPost = postRepo.countByUser(info.getId(),minTempDate,maxTempDate).orElse(0L);
            for (var post: allPost) {
                countsLike += likeRepo.countByUser(post.getId(),minTempDate,maxTempDate).orElse(0L);
                countsComment += commentRepo.countByUser(post.getId(),minTempDate,maxTempDate).orElse(0L);
            }
            chartUserDto.add(ChartUserDto.builder().date(minTempDate).posts(countPost).comments(countsComment).likes(countsLike).build());
        }
        return new BaseResponse(chartUserDto);
    }

    @Override
    public BaseResponse rankAuthor(Integer page, Integer size) {
        var pageReq = PageRequest.of(page -1,size);
        var exec = repo.rankAuthor(pageReq);
        var pageRes = PageResponse.builder()
                .data(exec.getContent().stream().map(userMapper::entityToProfile))
                .totalRecord(exec.getTotalElements())
                .totalPage(exec.getTotalPages())
                .pageNumber(page)
                .pageSize(size)
                .build();
        return new BaseResponse(pageRes);
    }


}

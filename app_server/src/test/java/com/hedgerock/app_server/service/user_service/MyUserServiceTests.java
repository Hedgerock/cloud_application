package com.hedgerock.app_server.service.user_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedgerock.app_server.config.constraints.types.TokenType;
import com.hedgerock.app_server.dto.Roles;
import com.hedgerock.app_server.dto.auth.RegisterDTO;
import com.hedgerock.app_server.dto.auth.validation.ValidationPasswordTokenDTO;
import com.hedgerock.app_server.dto.users.UserDTO;
import com.hedgerock.app_server.entity.AuthoritiesEntity;
import com.hedgerock.app_server.entity.UserEntity;
import com.hedgerock.app_server.exceptions.*;
import com.hedgerock.app_server.repository.AuthorityRepository;
import com.hedgerock.app_server.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.hedgerock.app_server.service.user_service.TestUtils.*;
import static com.hedgerock.app_server.service.user_service.TestUtils.getAuthoritiesEntity;
import static com.hedgerock.app_server.service.user_service.TestUtils.getValidationPasswordTokenDTO;
import static com.hedgerock.app_server.service.user_service.TestUtils.getValueAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyUserServiceTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MyUserService myUserService;

    @Test
    @DisplayName("Cache pending user for registration case when DTO somehow is damaged")
    void givenDamagedDTO_whenCachePendingUser_thenThrowsCacheValueException() throws Exception {
        //given
        final ValueOperations<String, String> valueOperations = getOpsForValue();
        final String token = getToken();
        final RegisterDTO registerDTO = getRegisterDTO();
        BDDMockito.given(objectMapper.writeValueAsString(registerDTO))
                .willThrow(JsonProcessingException.class);
        //when //then
        assertThatThrownBy(() ->
                myUserService.cachePendingUser(TokenType.CONFIRM_EMAIL, token, registerDTO))
                .isInstanceOf(CacheValueException.class)
                .hasMessageContaining(String.format("Failed to cache user %s", registerDTO.email()));

        verify(valueOperations, never())
                .set(anyString(), anyString(), any(Duration.class));
        verify(objectMapper, times(1))
                .writeValueAsString(registerDTO);
    }

    @Test
    @DisplayName("Cache pending user for registration")
    void givenTokenTypeTokenAndRegisterDTO_whenCachePendingUser_thenCachingTwoSideTokens() throws Exception {
        //given
        final ValueOperations<String, String> valueOperations = getOpsForValue();
        final String token = getToken();
        final String email = getEmail();
        final String firstKey = getKey(TokenType.CONFIRM_EMAIL, token);
        final String secondKey = getKey(TokenType.CONFIRM_EMAIL, email);
        final String valueAsString = getValueAsString();
        final RegisterDTO registerDTO = getRegisterDTO();

        BDDMockito.given(objectMapper.writeValueAsString(registerDTO))
                .willReturn(valueAsString);
        BDDMockito.given(redisTemplate.opsForValue())
                .willReturn(valueOperations);

        //when
        myUserService.cachePendingUser(TokenType.CONFIRM_EMAIL, token, registerDTO);
        //then
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Duration> ttlCaptor = ArgumentCaptor.forClass(Duration.class);

        verify(valueOperations, times(2))
                .set(keyCaptor.capture(), valueCaptor.capture(), ttlCaptor.capture());

        List<String> keys = keyCaptor.getAllValues();
        List<String> values = valueCaptor.getAllValues();

        assertThat(keys).containsExactlyInAnyOrder(firstKey, secondKey);
        assertThat(values).containsExactlyInAnyOrder(valueAsString, token);

        assertThat(ttlCaptor.getAllValues())
                .allMatch(ttl -> ttl.equals(Duration.ofMinutes(10)));

        verify(objectMapper, times(1))
                .writeValueAsString(registerDTO);
    }

    @Test
    @DisplayName("Cache pending email for password restore test")
    void givenTokenTypeTokenAndEmail_whenCachePendingUser_thenCachingTwoSideTokens() {
        //given
        final ValueOperations<String, String> valueOperations = getOpsForValue();
        final String token = getToken();
        final String email = getEmail();
        final String firstKey = getKey(TokenType.RESTORE_PASSWORD, token);
        final String secondKey = getKey(TokenType.RESTORE_PASSWORD, email);

        BDDMockito.given(redisTemplate.opsForValue())
                .willReturn(valueOperations);
        //when
        myUserService.cachePendingEmailForRestore(TokenType.RESTORE_PASSWORD, token, email);
        //then

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Duration> ttlCaptor = ArgumentCaptor.forClass(Duration.class);

        verify(valueOperations, times(2))
                .set(keyCaptor.capture(), valueCaptor.capture(), ttlCaptor.capture());

        List<String> keys = keyCaptor.getAllValues();
        List<String> values = valueCaptor.getAllValues();

        assertThat(keys).containsExactlyInAnyOrder(firstKey, secondKey);
        assertThat(values).containsExactlyInAnyOrder(email, token);

        assertThat(ttlCaptor.getAllValues())
                .allMatch(ttl -> ttl.equals(Duration.ofMinutes(10)));
    }

    @Test
    @DisplayName("Confirm user test case when failed to read value from redis")
    void givenDamagedValue_whenConfirmUser_thenThrowsUserConfirmException() throws Exception {
        //given
        final ValueOperations<String, String> valueOperations = getOpsForValue();
        final String token = getToken();
        final String key = getKey(TokenType.CONFIRM_EMAIL, token);
        final String valueAsString = getValueAsString();

        BDDMockito.given(redisTemplate.opsForValue())
                .willReturn(valueOperations);
        BDDMockito.given(valueOperations.get(key))
                .willReturn(valueAsString);
        BDDMockito.given(objectMapper.readValue(valueAsString, RegisterDTO.class))
                .willThrow(JsonProcessingException.class);
        //when then
        assertThatThrownBy(() -> myUserService.confirmUser(TokenType.CONFIRM_EMAIL, token))
                .isInstanceOf(UserConfirmException.class)
                .hasMessageContaining("Failed to confirm user");

        verify(valueOperations, times(1))
                .get(key);
        verify(objectMapper, times(1))
                .readValue(valueAsString, RegisterDTO.class);
        verify(authorityRepository, never())
                .findByAuthority(anyString());
        verify(userRepository, never())
                .save(any(UserEntity.class));
        verify(redisTemplate, never())
                .delete(ArgumentMatchers.<Collection<String>>any());
    }

    @Test
    @DisplayName("Confirm user test case when authority not found")
    void givenWrongAuthority_whenConfirmUser_thenThrowsRoleNotFoundException() throws Exception {
        //given
        final ValueOperations<String, String> valueOperations = getOpsForValue();
        final String token = getToken();
        final String key = getKey(TokenType.CONFIRM_EMAIL, token);
        final String valueAsString = getValueAsString();
        final RegisterDTO registerDTO = getRegisterDTO();

        BDDMockito.given(redisTemplate.opsForValue())
                .willReturn(valueOperations);
        BDDMockito.given(valueOperations.get(key))
                .willReturn(valueAsString);
        BDDMockito.given(objectMapper.readValue(valueAsString, RegisterDTO.class))
                .willReturn(registerDTO);
        BDDMockito.given(authorityRepository.findByAuthority(anyString()))
                .willReturn(Optional.empty());
        //when then
        assertThatThrownBy(() -> myUserService.confirmUser(TokenType.CONFIRM_EMAIL, token))
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining(String.format("Role %s not found", Roles.USER.name()));

        verify(valueOperations, times(1))
                .get(key);
        verify(objectMapper, times(1))
                .readValue(valueAsString, RegisterDTO.class);
        verify(authorityRepository, times(1))
                .findByAuthority(anyString());
        verify(userRepository, never())
                .save(any(UserEntity.class));
        verify(redisTemplate, never())
                .delete(ArgumentMatchers.<Collection<String>>any());
    }

    @Test
    @DisplayName("Confirm user test case when token is not valid")
    void givenInvalidToken_whenConfirmUser_thenThrowsInvalidTokenException() throws Exception {
        //given
        final ValueOperations<String, String> valueOperations = getOpsForValue();
        final String token = getToken();
        final String key = getKey(TokenType.CONFIRM_EMAIL, token);
        final String valueAsString = getValueAsString();

        BDDMockito.given(redisTemplate.opsForValue())
                .willReturn(valueOperations);
        BDDMockito.given(valueOperations.get(key))
                .willReturn(null);
        //when then
        assertThatThrownBy(() -> myUserService.confirmUser(TokenType.CONFIRM_EMAIL, token))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Token not found or not valid anymore");

        verify(valueOperations, times(1))
                .get(key);
        verify(redisTemplate, never())
                .delete(ArgumentMatchers.<Collection<String>>any());
        verify(objectMapper, never())
                .readValue(valueAsString, RegisterDTO.class);
        verify(authorityRepository, never())
                .findByAuthority(anyString());
        verify(userRepository, never())
                .save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Confirm user test")
    void givenTokenTypeAndToken_whenConfirmUser_thenSaveNewUserInDatabase() throws Exception {
        //given
        final RegisterDTO registerDTO = TestUtils.getRegisterDTO();
        final ValueOperations<String, String> valueOperations = getOpsForValue();
        final AuthoritiesEntity authoritiesEntity = getAuthoritiesEntity();
        final String valueAsString = getValueAsString();

        BDDMockito.given(redisTemplate.opsForValue())
                .willReturn(valueOperations);
        BDDMockito.given(valueOperations.get(anyString()))
                .willReturn(valueAsString);
        BDDMockito.given(objectMapper.readValue(valueAsString, RegisterDTO.class))
                .willReturn(registerDTO);
        BDDMockito.given(authorityRepository.findByAuthority(anyString()))
                .willReturn(Optional.of(authoritiesEntity));
        BDDMockito.given(redisTemplate.delete(ArgumentMatchers.<Collection<String>>any()))
                .willReturn(2L);
        //when
        myUserService.confirmUser(TokenType.CONFIRM_EMAIL, "9374b2bf-bcea-49d0-af03-f360f7b84240");
        //then

        verify(valueOperations, times(1))
                .get(anyString());
        verify(redisTemplate, times(1))
                .delete(ArgumentMatchers.<Collection<String>>any());
        verify(objectMapper, times(1))
                .readValue(valueAsString, RegisterDTO.class);
        verify(authorityRepository, times(1))
                .findByAuthority(anyString());
        verify(userRepository, times(1))
                .save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Confirm password test case when user is not found")
    void givenIncorrectUserData_whenConfirmPassword_thenThrowsUserNotFoundException() {
        //given
        final ValidationPasswordTokenDTO validationPasswordTokenDTO = getValidationPasswordTokenDTO();
        final ValueOperations<String, String> valueOperations = getOpsForValue();
        final String key = getKey(TokenType.RESTORE_PASSWORD, validationPasswordTokenDTO.token());

        BDDMockito.given(redisTemplate.opsForValue()).willReturn(valueOperations);
        BDDMockito.given(valueOperations.get(key)).willReturn("email@gmail.com");
        BDDMockito.given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        //when then
        assertThatThrownBy(() ->
                myUserService.confirmPassword(TokenType.RESTORE_PASSWORD, validationPasswordTokenDTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(String.format("User with email %s not found", "email@gmail.com"));

        verify(valueOperations, times(1))
                .get(anyString());
        verify(userRepository, times(1))
                .findByEmail(anyString());
        verify(redisTemplate, never())
                .delete(ArgumentMatchers.<Collection<String>>any());
        verify(passwordEncoder, never())
                .encode(anyString());
    }

    @Test
    @DisplayName("Confirm password test case when token is not valid")
    void givenInvalidToken_whenConfirmPassword_thenThrowsInvalidTokenException() {
        //given
        final ValidationPasswordTokenDTO validationPasswordTokenDTO = getValidationPasswordTokenDTO();
        final ValueOperations<String, String> valueOperations = getOpsForValue();
        BDDMockito.given(redisTemplate.opsForValue())
                .willReturn(valueOperations);

        BDDMockito
                .given(valueOperations.get(anyString()))
                .willReturn(null);
        //when then
        assertThatThrownBy(() ->
                myUserService.confirmPassword(TokenType.RESTORE_PASSWORD, validationPasswordTokenDTO))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Token not found or not valid anymore");

        verify(valueOperations, times(1))
                .get(anyString());
        verify(redisTemplate, never())
                .delete(ArgumentMatchers.<Collection<String>>any());
        verify(userRepository, never())
                .findByEmail(anyString());
        verify(passwordEncoder, never())
                .encode(anyString());
    }

    @Test
    @DisplayName("Confirm password test")
    void givenTokenTypeAndValidationPasswordToken_whenConfirmPassword_thenSaveChangesToDataBase() {
        //given
        final ValidationPasswordTokenDTO validationPasswordTokenDTO = getValidationPasswordTokenDTO();
        final UserEntity user = getUserEntity();
        final String password = getEncodedPassword();
        final ValueOperations<String, String> valueOperations = getOpsForValue();

        BDDMockito.given(redisTemplate.opsForValue())
                        .willReturn(valueOperations);

        BDDMockito
                .given(valueOperations.get(
                        TokenType.RESTORE_PASSWORD.getValue() + ":" + validationPasswordTokenDTO.token()))
                .willReturn("email@gmail.com");

        BDDMockito.given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(user));
        BDDMockito.given(passwordEncoder.encode(anyString()))
                .willReturn(password);

        BDDMockito.given(redisTemplate.delete(ArgumentMatchers.<Collection<String>>any()))
                .willReturn(2L);
        //when
        myUserService.confirmPassword(TokenType.RESTORE_PASSWORD, validationPasswordTokenDTO);
        //then
        assertThat(user.getPassword()).isEqualTo(password);

        Mockito.verify(valueOperations, times(1))
                .get(anyString());
        Mockito.verify(redisTemplate, times(1))
                .delete(ArgumentMatchers.<Collection<String>>any());

        Mockito.verify(userRepository, times(1))
                .findByEmail(anyString());
        Mockito.verify(passwordEncoder, times(1))
                .encode(anyString());
    }

    @Test
    @DisplayName("Get user by id test case when user not found")
    void givenInvalidUserId_whenGetUserById_thenThrowsUserNotFoundException() {
        //given
        final long FAKE_ID = 1L;
        BDDMockito.given(userRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when then
        assertThatThrownBy(() -> myUserService.getById(FAKE_ID))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(String.format("User with id %d not found", FAKE_ID));

        BDDMockito.then(userRepository).should().findById(anyLong());
    }

    @Test
    @DisplayName("Get user by id test")
    void givenUserId_whenGetUserById_thenReturnCurrentUser() {
        //given
        BDDMockito.given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(getUserEntity()));
        //when
        var result = myUserService.getById(anyLong());
        //then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(TestUtils.getUserDTO());

        BDDMockito.then(userRepository).should().findById(anyLong());
    }

    @Test
    @DisplayName("Get all users test case when list is empty")
    void givenEmptyList_whenRequestForGetAllUsers_thenReturnEmptyList() {
        //given
        BDDMockito.given(userRepository.findAll())
                .willReturn(Collections.emptyList());
        //when
        var result = myUserService.getAll();
        //then
        assertThat(result).isEmpty();

        BDDMockito.then(userRepository).should().findAll();
    }

    @Test
    @DisplayName("Get all users test")
    void givenListOfUsers_whenRequestForGetAllUsers_thenReturnListOfUsersDTO() {
        //given
        final List<UserEntity> entities = TestUtils.getUserEntities();
        final List<UserDTO> userDTOs = TestUtils.getUserDTOs();
        BDDMockito.given(userRepository.findAll())
                .willReturn(entities);

        //when
        var result = myUserService.getAll();
        //then
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        assertThat(result).isEqualTo(userDTOs);

        BDDMockito.then(userRepository).should().findAll();
    }
}

package com.hedgerock.app_server.service.email_service.test_utils;

import com.hedgerock.app_server.service.ServiceUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
public class LoadHtmlTest {

    @Test
    @DisplayName("Load html test case when path is invalid")
    void givenFilePath_whenLoadHtml_thenThrowsNoSuchElementException() {
        //given
        final String invalidPath = "invalid/html/path";
        //when then
        Assertions.assertThatThrownBy(() -> ServiceUtils.loadHTML(invalidPath))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Html with name " + invalidPath + " not found");
    }
}

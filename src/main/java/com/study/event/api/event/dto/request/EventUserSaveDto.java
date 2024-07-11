package com.study.event.api.event.dto.request;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventUserSaveDto {
    private String email;
    private String password;
}

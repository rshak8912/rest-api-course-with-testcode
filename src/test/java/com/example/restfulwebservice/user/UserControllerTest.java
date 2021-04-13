package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class UserControllerTest {
    @Autowired MockMvc mockMvc;

    @Autowired UserDaoService userDaoService;

    @Autowired ObjectMapper objectMapper;

    @Test
    public void retrieveAllUsers() throws Exception {
        mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(status().isOk());
    }
    @Test
    public void retrieveUser() throws Exception {
        mockMvc.perform(get("/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("name").value("Kenneth"))
                .andExpect(jsonPath("joinDate").exists())
                .andExpect(status().isOk());

    }
    @Test
    @Ignore
    public void createUserV1() throws Exception {
        User newUser = new User(null, "Kenneth1", new Date(), "pass1", "701010-1111111");


        mockMvc.perform(post("/v1/users")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newUser))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("4"))
                .andExpect(jsonPath("$.name").value("Kenneth1"))
                .andExpect(jsonPath("$.joinDate").exists())
                .andDo(print());
    }

    @Test
    public void createUserV2() throws Exception {
        User newUser = new User(null, "Kenneth1", new Date(), "pass1", "701010-1111111");

        mockMvc.perform(post("/v2/users")
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newUser))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
        long count = userDaoService.findAll().size();
        assertThat(count).isEqualTo(4);
    }
    @Ignore
    @Test
    public void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
        assertThat(userDaoService.findAll().size()).isEqualTo(2);
    }

}
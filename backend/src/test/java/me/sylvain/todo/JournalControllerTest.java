package me.sylvain.todo;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.sylvain.todo.controller.JournalController;
import me.sylvain.todo.persistence.cache.JournalCache;
import me.sylvain.todo.persistence.entity.Journal;
import me.sylvain.todo.persistence.repository.JournalRepository;

@WebMvcTest({JournalController.class})
@ActiveProfiles("test")
class JournalControllerTest {
    Journal journal = Journal.builder().id(1L).html("Added entry abc").createdAt(null).build(); 

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JournalRepository journalRepository;

    @MockitoBean
    private JournalCache journalCache;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        doNothing().when(journalCache).clearCache();
    }

    @Test
    void testGetAllEntries() throws Exception {
        when(journalRepository.findAll()).thenReturn(Collections.singletonList(journal));
        this.mockMvc.perform(get("/journals"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(mapper.writeValueAsString(journal))));
    }

    @Test
    void testGetLatestEntries() throws Exception {
        when(journalCache.getCache()).thenReturn(Collections.singletonList(journal));
        this.mockMvc.perform(get("/journals/latest"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(mapper.writeValueAsString(journal))));
    }
}

package me.sylvain.todo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import me.sylvain.todo.controller.MetricsController;
import me.sylvain.todo.shared.UniqueWordsCounter;

@WebMvcTest({MetricsController.class})
@ActiveProfiles("test")
class MetricsControllerTest {
    int metricsValue = 42; 

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UniqueWordsCounter uniqueWordsCounter;

    @BeforeEach
    public void beforeEach() {
    }

    @Test
    void testGetMetrics() throws Exception {
        when(uniqueWordsCounter.getValue()).thenReturn(metricsValue);
        String s = "# HELP unique_words Unique words\n";
        String s2 = "# TYPE unique_words gauge\n";
        String s3 = "unique_words " + metricsValue + "\n";
        this.mockMvc.perform(get("/metrics"))
            .andExpect(status().isOk())
            .andExpect(content().string(s + s2 + s3));
    }
}

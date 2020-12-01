package me.ivory.demorestdocs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import me.ivory.demorestdocs.books.BookController;
@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {
	
	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

	MockMvc mockMvc;
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(BookController.class)
				.apply(documentationConfiguration(restDocumentation))
				.build();
	}

	@Test
	public void getABook() throws Exception {
		mockMvc.perform(get("/book/1"))
			.andExpect(status().isOk())
			.andDo(document("get-a-book"));
	}
}

package de.rieckpil.courses.book.management;

import de.rieckpil.courses.config.WebSecurityConfig;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
// see
// https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.7-Release-Notes#migrating-from-websecurityconfigureradapter-to-securityfilterchain
@Import(WebSecurityConfig.class)
class BookControllerTest {

  @MockitoBean
  private BookManagementService bookManagementService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldGetEmptyArrayWhenNoBooksExists() throws Exception {
    this.mockMvc
      .perform(get("/api/books")
        .header(HttpHeaders.ACCEPT, "application/json"))
      .andExpect(status().isOk())
      .andExpect(content().contentType("application/json"))
      .andExpect(jsonPath("$.size()", Matchers.is(0)))
      .andDo(print())
      .andReturn();
  }

  @Test
  void shouldNotReturnXML() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get("/api/books")
        .header(HttpHeaders.ACCEPT, "application/xml"))
      .andExpect(status().isNotAcceptable());
  }

  @Test
  void shouldGetBooksWhenServiceReturnsBooks() throws Exception {

  }

  private Book createBook(
    Long id,
    String isbn,
    String title,
    String author,
    String description,
    String genre,
    Long pages,
    String publisher,
    String thumbnailUrl) {
    Book result = new Book();
    result.setId(id);
    result.setIsbn(isbn);
    result.setTitle(title);
    result.setAuthor(author);
    result.setDescription(description);
    result.setGenre(genre);
    result.setPages(pages);
    result.setPublisher(publisher);
    result.setThumbnailUrl(thumbnailUrl);
    return result;
  }
}

package de.rieckpil.courses.book.review;

import de.rieckpil.courses.config.WebSecurityConfig;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
// see
// https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.7-Release-Notes#migrating-from-websecurityconfigureradapter-to-securityfilterchain
@Import(WebSecurityConfig.class)
class ReviewControllerTest {

  @MockitoBean private ReviewService reviewService;

  @Autowired private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldReturnTwentyReviewsWithoutAnyOrderWhenNoParametersAreSpecified() throws Exception {
    ArrayNode result = objectMapper.createArrayNode();

    ObjectNode statistic = objectMapper.createObjectNode();
    statistic.put("bookId", 1L);
    statistic.put("isbn", "42");
    statistic.put("avg", 89.3);
    statistic.put("ratings", 2);

    result.add(statistic);

    when(reviewService.getAllReviews(20, "none")).thenReturn(result);

    mockMvc.perform(get("/api/books/reviews"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.size()", Matchers.is(1)));
  }

  @Test
  void shouldNotReturnReviewStatisticsWhenUserIsUnauthenticated() throws Exception {}

  @Test
  void shouldReturnReviewStatisticsWhenUserIsAuthenticated() throws Exception {}

  @Test
  void shouldCreateNewBookReviewForAuthenticatedUserWithValidPayload() throws Exception {}

  @Test
  void shouldRejectNewBookReviewForAuthenticatedUsersWithInvalidPayload() throws Exception {}

  @Test
  void shouldNotAllowDeletingReviewsWhenUserIsAuthenticatedWithoutModeratorRole()
      throws Exception {}

  @Test
  @WithMockUser(roles = "moderator")
  void shouldAllowDeletingReviewsWhenUserIsAuthenticatedAndHasModeratorRole() throws Exception {}
}

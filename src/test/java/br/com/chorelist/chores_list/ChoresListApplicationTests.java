package br.com.chorelist.chores_list;

import br.com.chorelist.chores_list.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChoresListApplicationTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testCreateTodoSuccess() {
        Todo todo = new Todo("todo1", "desc todo 1", false, 1);

        webTestClient
                .post()
                .uri("/todos")
                .bodyValue(todo)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].name").isEqualTo(todo.getName())
                .jsonPath("$[0].description").isEqualTo(todo.getDescription())
                .jsonPath("$[0].done").isEqualTo(todo.isDone())
                .jsonPath("$[0].priority").isEqualTo(todo.getPriority());
    }

    @Test
    void testCreateTodoFail() {
        Todo brokenTodo = new Todo("", "", false, 0);

        webTestClient
                .post()
                .uri("/todos")
                .bodyValue(brokenTodo)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateTodo() {
        Todo todo = new Todo("todo1", "desc todo 1", false, 1);

        webTestClient
                .post()
                .uri("/todos")
                .bodyValue(todo)
                .exchange()
                .expectStatus().isOk();

        todo.setDescription("updated desc todo 1");
        todo.setDone(true);
        todo.setPriority(2);

        webTestClient
                .put()
                .uri("/todos")
                .bodyValue(todo)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].description").isEqualTo("updated desc todo 1")
                .jsonPath("$[0].done").isEqualTo(true)
                .jsonPath("$[0].priority").isEqualTo(2);
    }

    @Test
    void testListTodos() {
        Todo todo1 = new Todo("todo1", "desc todo 1", false, 1);
        Todo todo2 = new Todo("todo2", "desc todo 2", false, 2);

        webTestClient
                .post()
                .uri("/todos")
                .bodyValue(todo1)
                .exchange()
                .expectStatus().isOk();

        webTestClient
                .post()
                .uri("/todos")
                .bodyValue(todo2)
                .exchange()
                .expectStatus().isOk();

        webTestClient
                .get()
                .uri("/todos")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].name").isEqualTo(todo2.getName())
                .jsonPath("$[1].name").isEqualTo(todo1.getName());
    }


}

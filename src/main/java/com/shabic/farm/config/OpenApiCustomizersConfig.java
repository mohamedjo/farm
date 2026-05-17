package com.shabic.farm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Configuration
public class OpenApiCustomizersConfig {

	@Bean
	public OpenApiCustomizer farmApiDocsCustomiser(
			OpenApiDocProperties props,
			@org.springframework.beans.factory.annotation.Value("classpath:api-examples/register_or_update_farm.json") Resource registerOrUpdateExample,
			@org.springframework.beans.factory.annotation.Value("classpath:api-examples/register_farm_minimal.json") Resource registerMinimalExample
	) {
		String registerOrUpdateJson = readUtf8(registerOrUpdateExample);
		String registerMinimalJson = readUtf8(registerMinimalExample);

		return openApi -> {
			addFarmTag(openApi, props);
			customizeFarmsPost(openApi, props, registerOrUpdateJson, registerMinimalJson);
			customizeFarmsPut(openApi, props, registerOrUpdateJson);
			customizeFarmsGetAll(openApi, props);
			customizeFarmsGetById(openApi, props);
			customizeFarmsDelete(openApi, props);
		};
	}

	private static String readUtf8(Resource resource) {
		try (var in = resource.getInputStream()) {
			return new String(in.readAllBytes(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException("Failed to read OpenAPI example: " + resource.getDescription(), e);
		}
	}

	private static void addFarmTag(OpenAPI openApi, OpenApiDocProperties props) {
		if (openApi.getTags() != null && openApi.getTags().stream().anyMatch(t -> props.tagFarmsName().equals(t.getName()))) {
			return;
		}
		openApi.addTagsItem(new io.swagger.v3.oas.models.tags.Tag()
				.name(props.tagFarmsName())
				.description(props.tagFarmsDescription()));
	}

	private static void customizeFarmsPost(OpenAPI openApi, OpenApiDocProperties props, String registerOrUpdateJson, String registerMinimalJson) {
		var path = openApi.getPaths() == null ? null : openApi.getPaths().get("/api/farms");
		if (path == null || path.getPost() == null) return;

		path.getPost().setTags(java.util.List.of(props.tagFarmsName()));
		path.getPost().setSummary("Register farm");
		path.getPost().setDescription("Creates a new farm. Response body is the generated UUID.");

		ensureJsonRequestExamples(path.getPost().getRequestBody(),
				Map.of(
						props.exampleRegisterFullName(), example(registerOrUpdateJson),
						props.exampleRegisterMinimalName(), example(registerMinimalJson)
				));

		ApiResponses responses = ensureResponses(path.getPost());
		responses.addApiResponse("201", new ApiResponse().description("Farm created")
				.content(new io.swagger.v3.oas.models.media.Content()
						.addMediaType("application/json", new MediaType().schema(new StringSchema().example(props.responseFarmIdExample())))));
		responses.addApiResponse("400", new ApiResponse().description("Validation error or duplicate registerId"));
	}

	private static void customizeFarmsPut(OpenAPI openApi, OpenApiDocProperties props, String registerOrUpdateJson) {
		var path = openApi.getPaths() == null ? null : openApi.getPaths().get("/api/farms/{id}");
		if (path == null || path.getPut() == null) return;

		path.getPut().setTags(java.util.List.of(props.tagFarmsName()));
		path.getPut().setSummary("Update farm");
		path.getPut().setDescription("Full replacement of editable fields. `id` and `createdAt` are preserved.");

		ensureJsonRequestExamples(path.getPut().getRequestBody(),
				Map.of(props.exampleUpdateName(), example(registerOrUpdateJson)));

		ApiResponses responses = ensureResponses(path.getPut());
		responses.addApiResponse("200", new ApiResponse().description("Updated farm"));
		responses.addApiResponse("400", new ApiResponse().description("Validation error or duplicate registerId"));
	}

	private static void customizeFarmsGetAll(OpenAPI openApi, OpenApiDocProperties props) {
		var path = openApi.getPaths() == null ? null : openApi.getPaths().get("/api/farms");
		if (path == null || path.getGet() == null) return;

		path.getGet().setTags(java.util.List.of(props.tagFarmsName()));
		path.getGet().setSummary("List all farms");
	}

	private static void customizeFarmsGetById(OpenAPI openApi, OpenApiDocProperties props) {
		var path = openApi.getPaths() == null ? null : openApi.getPaths().get("/api/farms/{id}");
		if (path == null || path.getGet() == null) return;

		path.getGet().setTags(java.util.List.of(props.tagFarmsName()));
		path.getGet().setSummary("Get farm by id");
	}

	private static void customizeFarmsDelete(OpenAPI openApi, OpenApiDocProperties props) {
		var path = openApi.getPaths() == null ? null : openApi.getPaths().get("/api/farms/{id}");
		if (path == null || path.getDelete() == null) return;

		path.getDelete().setTags(java.util.List.of(props.tagFarmsName()));
		path.getDelete().setSummary("Delete farm");
		path.getDelete().setDescription("Permanently removes the farm record.");
	}

	private static ApiResponses ensureResponses(io.swagger.v3.oas.models.Operation operation) {
		if (operation.getResponses() == null) {
			operation.setResponses(new ApiResponses());
		}
		return operation.getResponses();
	}

	private static void ensureJsonRequestExamples(RequestBody requestBody, Map<String, Example> examples) {
		if (requestBody == null || requestBody.getContent() == null) return;
		MediaType json = requestBody.getContent().get("application/json");
		if (json == null) return;
		if (json.getExamples() == null) {
			json.setExamples(new java.util.LinkedHashMap<>());
		}
		json.getExamples().putAll(examples);
	}

	private static Example example(String value) {
		return new Example().value(value);
	}
}

package io.led.hr.api.doc

import io.led.hr.security.TokenAuthConfig
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DocConfig {
    @Bean
    fun customOpenAPI(tokenAuthConfig: TokenAuthConfig): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("HR API")
                    .version("1.0.0")
            )
            .components(
                Components().addSecuritySchemes(
                    tokenAuthConfig.tokenHeaderName,
                    SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .`in`(SecurityScheme.In.HEADER)
                        .name(tokenAuthConfig.tokenHeaderName)
                )
            )
            .addSecurityItem(
                SecurityRequirement().addList(tokenAuthConfig.tokenHeaderName)
            )
    }
}

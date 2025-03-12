package community.whatever.onembackendkotlin.application

import community.whatever.onembackendkotlin.application.dto.BlockedDomainCheckRequest
import community.whatever.onembackendkotlin.application.dto.BlockedDomainCreateRequest
import community.whatever.onembackendkotlin.application.dto.BlockedDomainDeleteRequest
import community.whatever.onembackendkotlin.application.exception.DomainAlreadyBlockedException
import community.whatever.onembackendkotlin.domain.BlockedDomain
import community.whatever.onembackendkotlin.domain.BlockedDomainRepository
import community.whatever.onembackendkotlin.infra.repository.BlockedDomainInMemoryRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import kotlin.test.Test

class DefaultBlockedDomainServiceTest {

    private lateinit var blockedDomainService: BlockedDomainService
    private lateinit var blockedDomainRepository: BlockedDomainRepository

    @BeforeEach
    fun setUp() {
        blockedDomainRepository = BlockedDomainInMemoryRepository()
        blockedDomainService = DefaultBlockedDomainService(blockedDomainRepository)
    }

    @DisplayName("BlockedDomain 저장")
    @Nested
    inner class SaveBlockedDomain {

        private lateinit var url: String
        private lateinit var blockedDomain: BlockedDomain

        @BeforeEach
        fun setUp() {
            url = "https://www.google.com"
            blockedDomain = BlockedDomain(url)
        }

        @Test
        fun `저장된 도메인이 없으면 새로 저장하고 반환한다`() {
            // when
            val result = blockedDomainService.saveBlockedDomain(
                BlockedDomainCreateRequest(url)
            )

            // then
            assertThat(result.domain).isNotNull
        }

        @Test
        fun `이미 저장된 도메인이 있으면 예외를 발생시킨다`() {
            // given
            val request = BlockedDomainCreateRequest(url)
            blockedDomainService.saveBlockedDomain(request)

            // when
            assertThatThrownBy { blockedDomainService.saveBlockedDomain(request) }
                .isInstanceOf(DomainAlreadyBlockedException::class.java)
        }
    }

    @DisplayName("BlockedDomain 삭제")
    @Nested
    inner class DeleteBlockedDomain {

        private lateinit var url: String

        @BeforeEach
        fun setUp() {
            url = "https://www.google.com"
        }

        @Test
        fun `저장된 도메인을 삭제한다`() {
            // given
            blockedDomainService.saveBlockedDomain(BlockedDomainCreateRequest(url))

            // when
            blockedDomainService.deleteBlockedDomain(BlockedDomainDeleteRequest(url))

            // then
            assertThat(blockedDomainService.isBlockedDomain(BlockedDomainCheckRequest(url))).isFalse()
        }
    }

    @DisplayName("BlockedDomain 조회")
    @Nested
    inner class GetAllBlockedDomains {

        private lateinit var url: String

        @BeforeEach
        fun setUp() {
            url = "https://www.google.com"
        }

        @Test
        fun `저장된 모든 도메인을 조회한다`() {
            // given
            blockedDomainService.saveBlockedDomain(BlockedDomainCreateRequest(url))

            // when
            val result = blockedDomainService.getAllBlockedDomains()

            // then
            assertThat(result).isNotEmpty
        }
    }

    @DisplayName("BlockedDomain 조회")
    @Nested
    inner class IsBlockedDomain {

        private lateinit var url: String

        @BeforeEach
        fun setUp() {
            url = "https://www.google.com"
        }

        @Test
        fun `저장된 도메인이 존재하는지 확인한다`() {
            // given
            blockedDomainService.saveBlockedDomain(BlockedDomainCreateRequest(url))

            // when
            val result = blockedDomainService.isBlockedDomain(BlockedDomainCheckRequest(url))

            // then
            assertThat(result).isTrue()
        }

        @Test
        fun `저장된 도메인이 존재하지 않는지 확인한다`() {
            // when
            val result = blockedDomainService.isBlockedDomain(BlockedDomainCheckRequest(url))

            // then
            assertThat(result).isFalse()
        }
    }
}

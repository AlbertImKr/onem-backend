package community.whatever.onembackendkotlin.infra.repository

import community.whatever.onembackendkotlin.domain.BlockedDomain
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class BlockedDomainInMemoryRepositoryTest {

    @Test
    fun `BlockedDomain를 저장하고 조회할 수 있다`() {
        // given
        val blockedDomain = BlockedDomain("blocked.com", UUID.randomUUID())
        val repository = BlockedDomainInMemoryRepository()

        // when
        repository.save(blockedDomain)

        // then
        assertThat(repository.existsByDomain("blocked.com")).isTrue
        assertThat(repository.findAll()).containsExactly(blockedDomain)
    }

    @Test
    fun `BlockedDomain를 삭제할 수 있다`() {
        // given
        val blockedDomain = BlockedDomain("blocked.com", UUID.randomUUID())
        val repository = BlockedDomainInMemoryRepository()
        repository.save(blockedDomain)

        // when
        repository.deleteByDomain("blocked.com")

        // then
        assertThat(repository.existsByDomain("blocked.com")).isFalse
        assertThat(repository.findAll()).isEmpty()
    }

    @Test
    fun `BlockedDomain가 존재하지 않으면 false를 반환한다`() {
        // given
        val repository = BlockedDomainInMemoryRepository()

        // when
        val result = repository.existsByDomain("blocked.com")

        // then
        assertThat(result).isFalse
    }

    @Test
    fun `모든 BlockedDomain를 조회할 수 있다`() {
        // given
        val blockedDomain1 = BlockedDomain("blocked1.com", UUID.randomUUID())
        val blockedDomain2 = BlockedDomain("blocked2.com", UUID.randomUUID())
        val repository = BlockedDomainInMemoryRepository()
        repository.save(blockedDomain1)
        repository.save(blockedDomain2)

        // when
        val result = repository.findAll()

        // then
        assertThat(result).containsExactly(blockedDomain1, blockedDomain2)
    }
}

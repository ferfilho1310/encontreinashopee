package br.com.encontreinashopee.repository.cupoms

import br.com.encontreinashopee.model.CupomModel
import kotlinx.coroutines.flow.Flow

interface CupomRepository {

    suspend fun getCupoms(): Flow<List<CupomModel>>
}
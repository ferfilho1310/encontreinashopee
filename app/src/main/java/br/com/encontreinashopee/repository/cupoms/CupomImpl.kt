package br.com.encontreinashopee.repository.cupoms

import br.com.encontreinashopee.model.CupomModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CupomImpl(
    val api: CupomApi
) : CupomRepository {
    override suspend fun getCupoms(): Flow<List<CupomModel>> = flow {
        emit(api.getCupom())
    }
}
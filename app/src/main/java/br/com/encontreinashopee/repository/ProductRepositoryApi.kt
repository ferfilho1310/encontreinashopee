package br.com.encontreinashopee.repository

import br.com.encontreinashopee.model.OfferCardModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ProductRepositoryApi : ProductRespositoryApiContract {

    val firebase = FirebaseFirestore.getInstance()

    override fun searchExclusiveOffers(): Flow<ArrayList<OfferCardModel>> = callbackFlow {
        val db = firebase
            .collection("exclusiveOffers")
            .get()
            .addOnSuccessListener {
                val exclusiveOffersList = it.map { it.toObject(OfferCardModel::class.java) }
                trySend(exclusiveOffersList as ArrayList).isSuccess
            }.addOnFailureListener {
                trySend(arrayListOf()).isFailure
            }
        awaitClose {
            db.isCanceled
        }
    }

    override fun searchOffers(): Flow<ArrayList<OfferCardModel>> = callbackFlow {
        val db = firebase
            .collection("offers")
            .get()
            .addOnSuccessListener {
                val exclusiveOffersList = it.map { it.toObject(OfferCardModel::class.java) }
                trySend(exclusiveOffersList as ArrayList).isSuccess
            }.addOnFailureListener {
                trySend(arrayListOf()).isFailure
            }
        awaitClose {
            db.isCanceled
        }
    }
}
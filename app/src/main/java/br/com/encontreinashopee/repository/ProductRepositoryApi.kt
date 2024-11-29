package br.com.encontreinashopee.repository

import br.com.encontreinashopee.model.OfferCardModel
import br.com.encontreinashopee.model.OfferStoriesModel
import br.com.encontreinashopee.model.OfferVideoModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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

    override fun searchStoriesOffers(): Flow<ArrayList<OfferStoriesModel>> = callbackFlow {
        val db = firebase
            .collection("offersStories")
            .get()
            .addOnSuccessListener {
                val offerStories = it.map { it.toObject(OfferStoriesModel::class.java) }
                trySend(offerStories as ArrayList).isSuccess
            }.addOnFailureListener {
                trySend(arrayListOf()).isFailure
            }
        awaitClose {
            db.isCanceled
        }
    }

    override fun searchVideoProductOffer(): Flow<ArrayList<OfferVideoModel>> = callbackFlow {
        val db = firebase
            .collection("offervideo")
            .get()
            .addOnSuccessListener {
                val exclusiveOffersList = it.map { it.toObject(OfferVideoModel::class.java) }
                trySend(exclusiveOffersList as ArrayList).isSuccess
            }.addOnFailureListener {
                trySend(arrayListOf()).isFailure
            }
        awaitClose {
            db.isCanceled
        }
    }
}
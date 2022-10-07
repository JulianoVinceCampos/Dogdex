package com.julianovincecampos.dogedex.di

class GoodRepository : RepositoryInterface {

    override fun downloadData(): String {
        val data = "Real data from server"
        return data
    }

}
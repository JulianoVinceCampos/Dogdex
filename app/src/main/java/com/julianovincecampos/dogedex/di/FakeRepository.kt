package com.julianovincecampos.dogedex.di

class FakeRepository : RepositoryInterface {

    override fun downloadData(): String {
        val data = "Fake data to test"
        return data
    }

}
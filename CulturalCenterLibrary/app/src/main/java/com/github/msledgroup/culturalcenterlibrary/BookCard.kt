package com.github.msledgroup.culturalcenterlibrary

class BookCard {

    private lateinit var title: String;

    private lateinit var coverImg: String;


    public fun BookCard(){

    }

    public fun BookCard(title: String, coverImg: String){
        this.title = title
        this.coverImg = coverImg
    }

    //getters
    fun getTitle() : String {
        return title
    }
    fun getCoverImg(): String {
        return coverImg
    }
    //Setters
    fun setTitle(title: String){
        this.title = title
    }
    fun setCoverImg(coverImg: String){
        this.coverImg = coverImg
    }


}
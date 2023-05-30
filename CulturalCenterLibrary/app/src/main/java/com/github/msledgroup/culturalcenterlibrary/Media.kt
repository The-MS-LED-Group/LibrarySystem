package com.github.msledgroup.culturalcenterlibrary


abstract class Media(var title: String, var stock: Int){
    
}


class Book(title: String, stock: Int, var author: String, var edition: String, var publication: String, var genre: String, var ageLvl: String ) : Media(title, stock) {

}

class Game{

}

class Magazine{

}

class Video{

}
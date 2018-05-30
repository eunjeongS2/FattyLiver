package kr.ac.ajou.fattyliver.model

class User(var uid: String = "", var name: String = "", var email: String = "", var password: String = ""){
    companion object {
        fun newUser(uid: String, name: String, email: String, password: String): User {
            return User(uid, name, email, password)
        }
    }
}


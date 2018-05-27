package kr.ac.ajou.fattyliver.model

class UserSelect(var name: String, var userId: String, var selected: Boolean){
    companion object {
        fun newUserSelect(name: String, userId: String, selected: Boolean): UserSelect {
            return UserSelect(name, userId, selected)
        }
    }
}

package kr.ac.ajou.fattyliver.model
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserModel {
    var user: User? = User()
    var onLoginListener: OnLoginListener? = null
    var onSignUpListener: OnSignUpListener? = null
    private var ref : DatabaseReference? = null
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private object Singleton {
        val INSTANCE = UserModel()
    }

    companion object {
        val instance: UserModel
            get() = Singleton.INSTANCE
    }

    interface OnLoginListener {
        fun onSuccess()
        fun onFail()
    }

    interface OnSignUpListener {
        fun onSuccess()
        fun onFail()
    }

    fun login(email : String, password : String){
        val authResultTask = auth.signInWithEmailAndPassword(email, password)

        authResultTask.addOnSuccessListener { p0 ->

            getUser(p0.user?.uid!!)
        }

        authResultTask.addOnFailureListener {
            onLoginListener?.onFail()
        }
    }

    private fun getUser(uid: String){
        ref = database.getReference("user").child(uid).child("info")


        this.ref?.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                println(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user: User? = p0.getValue(User::class.java)
                instance.user = user

                onLoginListener?.onSuccess()
            }

        })
    }

    private fun addUser(uid: String, name: String, email: String, password: String){
        ref = database.getReference("user").child(uid).child("info")

        ref?.child("uid")?.setValue(uid)
        ref?.child("name")?.setValue(name)
        ref?.child("email")?.setValue(email)
        ref?.child("password")?.setValue(password)

    }

    fun signUp(name: String, email: String, password: String){
        val authResultTask = auth.createUserWithEmailAndPassword(email, password)
        authResultTask.addOnCompleteListener { p0 ->
            if (p0.isSuccessful){
                onSignUpListener?.onSuccess()
                addUser(p0.result.user.uid, name, email, password)
            } else
                onSignUpListener?.onFail()
        }
    }
}
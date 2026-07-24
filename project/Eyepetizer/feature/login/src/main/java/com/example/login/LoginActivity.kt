package com.example.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.data.store.SPUtils
import com.example.login.databinding.ActivityLoginBinding

class LoginActivity : ComponentActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SPUtils.init(this)
        checkAutoLogin()
        initClick()
    }

    fun checkAutoLogin(){
        binding.cbAutologin.isChecked =  SPUtils.getBool("autologin")


        if (SPUtils.getBool("autologin") ){
            binding.ivLoginAccount.setText(SPUtils.getString("last_account"))
            binding.ivLoginPassword.setText(SPUtils.getString("last_password"))

            if (checkPassword()) {
                Log.d("TAG", "checkAutoLogin: 自动登录成功")
                login()
            }
        }

    }

    fun login(){
        //ARouter.getInstance().build("/feature/home/MainActivity").navigation()
    }

    fun initClick(){
        binding.cbAutologin.setOnCheckedChangeListener { button, bool ->
            if (bool){
                SPUtils.putBool("autologin",bool)
            }else{
                SPUtils.putBool("autologin",bool)
            }
        }

        binding.imgLoginBtn.setOnClickListener {
           if (checkPassword()){
               Toast.makeText(this,"登录成功！", Toast.LENGTH_SHORT)
               Log.d("TAG", "initClick: 登录成功！")
               login()
           }
        }
    }

    fun checkAccount(a:String): Boolean{
        return (SPUtils.getString("account_$a")!=null)
    }

    fun checkPassword(): Boolean{
        var safe=false
        val account = binding.ivLoginAccount.text.toString()
        val password = binding.ivLoginPassword.text.toString()
        Log.d("TAG", "account:$account ")
        Log.d("TAG", "password:$password ")
        if (!checkWord(password)){
            Toast.makeText(this,"密码不合法！", Toast.LENGTH_SHORT)
            Log.d("TAG", "checkPassword:密码不合法！ ")
            return false
        }

        if (!checkAccount(account)){
            SPUtils.putString("account_$account",password)
            safe = true
        }

        if (SPUtils.getString("account_$account")==password){
            safe = true
        }else{
            Toast.makeText(this,"密码不正确！", Toast.LENGTH_SHORT)
            Log.d("TAG", "checkPassword:密码不正确！ ")
            safe = false
        }

        if (safe){
            SPUtils.putString("last_account",account)
            SPUtils.putString("last_password",password)
        }

        return safe
    }

    fun checkWord(s:String): Boolean{
        val r = "^[a-zA-Z0-9]{6,16}$"

        return s.matches(r.toRegex())
    }
}


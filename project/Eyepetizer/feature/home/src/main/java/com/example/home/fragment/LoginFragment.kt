package com.example.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.data.store.SPUtils
import com.example.home.R
import com.example.home.databinding.FragmentLoginBinding
import com.therouter.TheRouter

class LoginFragment : Fragment(){
    lateinit var binding: FragmentLoginBinding
    private var account = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG", "LoginFragment: 跳转登录页")
        initClick()
    }

    fun login(){
        SPUtils.putBool("isLogin",true)
        findNavController().navigate(R.id.MineFragment)
    }

    fun initClick(){

        binding.imgLoginBtn.setOnClickListener {
            if (checkPassword()){
                Toast.makeText(requireContext(),"登录成功！", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(),"密码不合法！", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(),"密码不正确！", Toast.LENGTH_SHORT).show()
            Log.d("TAG", "checkPassword:密码不正确！ ")
            safe = false
        }

        if (safe){
            SPUtils.putString("last_account",account)
            SPUtils.putString("last_password",password)
            this.account = account
        }

        return safe
    }

    fun checkWord(s:String): Boolean{
        val r = "^[a-zA-Z0-9]{6,16}$"

        return s.matches(r.toRegex())
    }
}
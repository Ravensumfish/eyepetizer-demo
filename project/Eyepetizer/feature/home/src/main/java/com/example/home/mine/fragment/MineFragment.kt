package com.example.home.mine.fragment

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.data.store.SPUtils
import com.example.home.R
import com.example.home.databinding.FragmentMineBinding
import java.io.File

class MineFragment : Fragment() {

    private var _binding: FragmentMineBinding? = null
    private val binding get()= _binding!!

    var account: String? = null

    private var name: String? = null
    private var description: String? = null

    //注册图片选择启动器，用户选择完图片后触发回调
    private var pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.imgMineAvatar.setImageURI(uri)
                saveAvatar(uri)
            } else {
                Log.d("TAG", "媒体选择回调: 未选择图片")
            }
        }

    lateinit var resolver: ContentResolver


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initClick()
    }

    override fun onDestroyView() {
        _binding =null
        super.onDestroyView()
    }

    fun init() {
        checkLogin()
        binding.tvMineName.text = "开眼用户"
        binding.tvMineDescription.text = "个人签名"
        account = SPUtils.getString("last_account")
        Log.d("TAG", "initmine:拿到account$account ")

        name = SPUtils.getString("name_$account")
        description = SPUtils.getString("des_$account")
        if (name != null && description != null){
            binding.tvMineName.text = name
            binding.tvMineDescription.text = description
        }

        loadAvatar()
    }


    fun checkLogin() {
        val b = SPUtils.getBool("isLogin")
        Log.d("TAG", "checkLogin: 是否已经登录：$b")
        if (!b) {
            findNavController().navigate(R.id.LoginFragment)
        }
    }

    fun initClick() {
        binding.imgMineEdit.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater

            val dialog = inflater.inflate(R.layout.dialog_edit, null)

            val etName: EditText = dialog.findViewById(R.id.et_dialog_name)
            val etDes: EditText = dialog.findViewById(R.id.et_dialog_description)

            etName.setText(name)
            etDes.setText(description)

            builder.setView(dialog)
                .setTitle("编辑用户信息")
                .setPositiveButton("确认") { _, _ ->
                    val newName = etName.text.toString()
                    val newDes = etDes.text.toString()

                    save(newName, newDes)
                }
                .setNegativeButton("取消", null)
                .show()
        }

        binding.tvMineStar.setOnClickListener {
            val bundle = bundleOf(
                "account" to account
            )
            findNavController().navigate(R.id.myStarFragment, bundle)
        }

        binding.tvQuitLogin.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("提示")
                .setMessage("确定要退出登录吗？")
                .setPositiveButton("确定") { _, _ ->
                    SPUtils.putBool("isLogin", false)
                    SPUtils.putString("last_account", "")
                    SPUtils.putString("last_password", "")
                    findNavController().popBackStack(R.id.fragment_home, false)
                }
                .setNegativeButton("取消", null)
                .show()
        }

        binding.imgMineAvatar.setOnClickListener {

            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
    }


    fun save(name: String, des: String) {
        SPUtils.putString("name_$account", name)
        SPUtils.putString("des_$account", des)
        binding.tvMineName.text = name
        binding.tvMineDescription.text = des
        Log.d("TAG", "save name:${SPUtils.getString("name_$account")} ")
        Log.d("TAG", "save des:${SPUtils.getString("des_$account")} ")
    }

    fun saveAvatar(uri: Uri) {


        resolver = requireContext().contentResolver

        try {
            resolver.openInputStream(uri)?.use { input ->
                //本地存入路径
                val file = File(requireContext().filesDir, "avatar_$account.jpg")
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
                SPUtils.putString("avatar_$account", file.absolutePath)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadAvatar() {
        val path = SPUtils.getString("avatar_$account")
        if (path != null && File(path).exists()) {
            //使用glide加载图片
            Glide.with(requireContext())
                .load(File(path))
                .placeholder(com.example.ui.R.color.gray)
                .into(binding.imgMineAvatar)
        } else {
            binding.imgMineAvatar.setImageResource(com.example.ui.R.color.gray)
        }
    }

}
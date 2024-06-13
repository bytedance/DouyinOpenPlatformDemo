package com.bytedance.sdk.douyin.open.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bytedance.sdk.douyin.databinding.FragmentDouYinProfileBinding
import com.bytedance.sdk.douyin.open.ability.auth.DouyinLoginManager
import com.bytedance.sdk.open.aweme.core.OpenCallback
import com.bytedance.sdk.open.aweme.init.DouYinSdkContext
import com.bytedance.sdk.open.aweme.openprofile.api.DouYinProfileApi
import com.bytedance.sdk.open.aweme.openprofile.api.ProfileCallback
import com.bytedance.sdk.open.aweme.openprofile.openmodel.OpenProfileModel
import com.bytedance.sdk.open.aweme.openprofile.openmodel.OpenVideoInfo
import com.bytedance.sdk.open.aweme.openprofile.openmodel.RequestModel
import com.bytedance.sdk.open.aweme.openticket.api.DouYinTicketApi
import com.bytedance.sdk.open.aweme.openticket.openmodel.AccessTicketData
import com.bytedance.sdk.open.aweme.utils.GsonUtil
import com.bytedance.sdk.open.aweme.utils.ThreadUtils
import com.bytedance.sdk.open.douyin.DouYinOpenApiFactory
import com.google.gson.GsonBuilder

class DouYinProfileFragment : Fragment() {

    companion object {
        fun newInstance() = DouYinProfileFragment()
    }

    private var _binding: FragmentDouYinProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: DouYinProfileViewModel
    private val douYinProfileApi: DouYinProfileApi by lazy {
        DouYinOpenApiFactory.createOpenApi(context, DouYinProfileApi::class.java)
    }

    private val prettyGson = GsonBuilder().setPrettyPrinting().create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDouYinProfileBinding.inflate(inflater, container, false)
        binding.hostOpenidEdit.apply {
            DouyinLoginManager.inst().douYinUser?.let {
                setText(it.openId)
            }
        }
        binding.clientOpenidEdit.apply {
            DouyinLoginManager.inst().clientDouYinUserLiveData.value?.let {
                setText(it.openId)
            }
        }
        binding.fetchCardModelBt.apply {
            setOnClickListener {
                val requestModel = buildRequestModel()
                douYinProfileApi.fetchDouYinCardModel(context, requestModel, object : ProfileCallback<OpenProfileModel> {
                    override fun onFail(p0: Int, p1: String?) {
                        setResult("fetchDouYinCardModel onFail code=$p0 msg=$p1")
                    }

                    override fun onSuccess(p0: OpenProfileModel?) {
                        val toJson = prettyGson.toJson(p0)
                        setResult("fetchDouYinCardModel onSuccess  model=${toJson}")
                        ThreadUtils.postInMain {
                            p0?.let {
                                var sb = StringBuilder()
                                it.openVideoInfos?.forEach {
                                    if (!sb.isEmpty()) {
                                        sb.append(",")
                                    }
                                    sb.append(it.videoId)
                                }
                                binding.videoListEdit.setText(sb.toString())
                            }
                        }
                    }
                })
            }
        }
        binding.getVideoListBt.setOnClickListener {
            val requestModel = buildRequestModel()
            val list = binding.videoListEdit.editableText.toString().split(",")
            douYinProfileApi.getVideoUrlList(requireContext(), requestModel, list, object : ProfileCallback<List<OpenVideoInfo>> {
                override fun onFail(p0: Int, p1: String?) {
                    setResult("getVideoUrlList onFail code=$p0 msg=$p1")
                }

                override fun onSuccess(p0: List<OpenVideoInfo>?) {
                    val toJson = prettyGson.toJson(p0)
                    setResult("getVideoUrlList onSuccess  model=${toJson}")
                }
            })
        }
        binding.switchModeBt.setOnClickListener {
            val requestModel = buildRequestModel()
            val mode = binding.switchModeEdit.editableText.toString()
            douYinProfileApi.switchCardShowMode(requireContext(), requestModel, mode, object : ProfileCallback<Boolean> {
                override fun onFail(p0: Int, p1: String?) {
                    setResult("switchCardShowMode onFail code=$p0 msg=$p1")
                }

                override fun onSuccess(p0: Boolean?) {
                    setResult("switchCardShowMode onSuccess  model=${p0}")
                }
            })
        }
        binding.updateCoverVideoBt.setOnClickListener {
            val requestModel = buildRequestModel()
            val videoId = binding.coverVideoEdit.editableText.toString()
            douYinProfileApi.updateCoverVideo(requireContext(), requestModel, videoId, object : ProfileCallback<Boolean> {
                override fun onFail(p0: Int, p1: String?) {
                    setResult("updateCoverVideo onFail code=$p0 msg=$p1")
                }

                override fun onSuccess(p0: Boolean?) {
                    setResult("updateCoverVideo onSuccess  model=${p0}")
                }
            })
        }
        initTicketView()
        return _binding!!.root
    }

    private fun initTicketView() {
        binding.getClientTicketBt.setOnClickListener {
            val ticketApi = DouYinOpenApiFactory.createOpenApi(requireContext(), DouYinTicketApi::class.java)
            val clientTicket = ticketApi.getClientTicket(DouYinSdkContext.inst().clientKey)
            setResult("clientTicket=$clientTicket")
        }
        binding.removeClientTicketBt.setOnClickListener {
            val ticketApi = DouYinOpenApiFactory.createOpenApi(requireContext(), DouYinTicketApi::class.java)
            ticketApi.removeClientTicket(DouYinSdkContext.inst().clientKey)
            setResult("removeClientTicket success")
        }
        binding.updateClientTicketBt.setOnClickListener {
            val ticketApi = DouYinOpenApiFactory.createOpenApi(requireContext(), DouYinTicketApi::class.java)
            ticketApi.tryUpdateClientTicket(DouYinSdkContext.inst().clientKey, object : OpenCallback<String> {
                override fun onFail(p0: Int, p1: String?) {
                    setResult("tryUpdateClientTicket onFail code=$p0 msg=$p1")
                }

                override fun onSuccess(p0: String?) {
                    setResult("tryUpdateClientTicket onSuccess ticket=${p0}")
                }
            })

        }
        binding.getAccessTicketBt.setOnClickListener {
            val ticketApi = DouYinOpenApiFactory.createOpenApi(requireContext(), DouYinTicketApi::class.java)
            val hostOpenId = binding.hostOpenidEdit.editableText?.toString()
            val accessTicket = ticketApi.getAccessTicket(DouYinSdkContext.inst().clientKey, hostOpenId)
            setResult(
                "getAccessTicket accessTicket=${
                    try {
                        GsonUtil.getGson().toJson(accessTicket)
                    } catch (e: Exception) {
                        null
                    }
                }"
            )
        }
        binding.removeAccessTicketBt.setOnClickListener {
            val ticketApi = DouYinOpenApiFactory.createOpenApi(requireContext(), DouYinTicketApi::class.java)
            val hostOpenId = binding.hostOpenidEdit.editableText?.toString()
            ticketApi.removeAccessTicket(DouYinSdkContext.inst().clientKey, hostOpenId)
            setResult("removeAccessTicket success")
        }
        binding.updateAccessTicketBt.setOnClickListener {
            val ticketApi = DouYinOpenApiFactory.createOpenApi(requireContext(), DouYinTicketApi::class.java)
            val hostOpenId = binding.hostOpenidEdit.editableText?.toString()
            ticketApi.tryUpdateAccessTicket(DouYinSdkContext.inst().clientKey, hostOpenId, object : OpenCallback<AccessTicketData> {
                override fun onFail(p0: Int, p1: String?) {
                    setResult("tryUpdateAccessTicket onFail code=$p0 msg=$p1")
                }

                override fun onSuccess(p0: AccessTicketData?) {
                    setResult(
                        "tryUpdateAccessTicket accessTicket=${
                            try {
                                GsonUtil.getGson().toJson(p0)
                            } catch (e: Exception) {
                                null
                            }
                        }"
                    )
                }

            })
        }
    }

    private fun buildRequestModel(): RequestModel {
        val hostOpenId = binding.hostOpenidEdit.editableText?.toString()
        val clientOpenId = binding.clientOpenidEdit.editableText?.toString()
        val isHost = binding.isHostSwitch.isChecked
        return RequestModel.Builder()
            .setHostOpenId(hostOpenId)
            .apply {
                if (isHost) {
                    setShowOpenId(hostOpenId)
                } else {
                    setShowOpenId(clientOpenId)
                }
            }
            .build()
    }

    private fun setResult(result: String) {
        ThreadUtils.postInMain {
            binding.resultText.text = result
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DouYinProfileViewModel::class.java)
    }

}
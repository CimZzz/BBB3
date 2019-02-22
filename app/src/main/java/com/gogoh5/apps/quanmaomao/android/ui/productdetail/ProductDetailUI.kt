package com.gogoh5.apps.quanmaomao.android.ui.productdetail

import android.animation.ArgbEvaluator
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.alibaba.baichuan.android.trade.AlibcTrade
import com.alibaba.baichuan.android.trade.AlibcTradeSDK
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback
import com.alibaba.baichuan.android.trade.model.AlibcShowParams
import com.alibaba.baichuan.android.trade.model.OpenType
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage
import com.alibaba.baichuan.android.trade.page.AlibcPage
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams
import com.bigkoo.convenientbanner.ConvenientBanner
import com.bigkoo.convenientbanner.listener.OnPageChangeListener
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.base.BaseUI
import com.gogoh5.apps.quanmaomao.library.entities.databeans.ProductDetailBean
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.environment.constants.ActionSource
import com.gogoh5.apps.quanmaomao.library.environment.constants.SourceType
import com.gogoh5.apps.quanmaomao.library.extensions.forEach
import com.gogoh5.apps.quanmaomao.library.extensions.setSize
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import com.gogoh5.apps.quanmaomao.library.utils.StringUtils
import com.gogoh5.apps.quanmaomao.library.utils.ViewUtils
import com.gogoh5.apps.quanmaomao.library.widgets.OnceImageView
import kotlinx.android.synthetic.main.sub_product_detail_content.view.*
import kotlinx.android.synthetic.main.ui_product_detail.*

class ProductDetailUI: BaseUI<ProductDetailPresenter>(), IProductDetailView {

    companion object {
        const val TOP_ANIMATION_LENGTH = 100
        const val LAZY_LOAD_DETAIL = 0
    }

    private val argbEvaluator = ArgbEvaluator()

    override fun initPresenter(): ProductDetailPresenter = ProductDetailPresenter(this)

    override fun initView() {
        SysContext.makeNavigationTransparent(this)
        setContentView(R.layout.ui_product_detail)

        topGuide.setSize(height = SysContext.getStatusBarHeight())

        backBtn.tapWith("商品详情页返回按钮", ActionSource.PRODUCT_DETAIL_BACK) {
            finish()
        }

        viewHandler.viewStubFirstBind = {
            view, id->
            when(id) {
                R.id.error -> {
                    view.tapWith("商品详情页失败重试", ActionSource.PRODUCT_DETAIL_HELP) {
                        presenter.requestProductDetail()
                    }
                }

                R.id.content -> {
                    view.scrollView.listener = {
                        _, t, _, _ ->
                        processScrollY(t)
                    }

                    view.bannerView.onPageChangeListener = object: OnPageChangeListener {
                        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {}
                        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {}

                        override fun onPageSelected(index: Int) {
                            processBannerChanged(index)
                        }
                    }


                    view.rewardHelpBtn.tapWith("商品详情页帮助按钮", ActionSource.PRODUCT_DETAIL_HELP) {
                        topGuide.tag = true
                        topGuide.setBackgroundColor(Color.BLACK)
                        RewardHelperDialog(this) {
                            topGuide.tag = false
                            processScrollY(view.scrollView.scrollY)
                        }.show()
                    }

                    view.couponBtn.tapWith("商品详情页领券按钮", ActionSource.PRODUCT_DETAIL_COUPON_BUY) {
                        presenter.buy()
                    }

                    view.buyBg.tapWith("商品详情页购买按钮", ActionSource.PRODUCT_DETAIL_BOTTOM_BUY) {
                        presenter.buy()
                    }

                    view.homeBg.tapWith("商品详情页首页按钮", ActionSource.PRODUCT_DETAIL_BOTTOM_HOME) {
                        presenter.linkHome()
                    }

                    view.cartBg.tapWith("商品详情页购物车按钮", ActionSource.PRODUCT_DETAIL_BOTTOM_CART) {
                        presenter.linkCart()
                    }
                }
            }
        }
    }

    override fun showLoading() {
        viewHandler.showView(R.id.loading)
    }

    override fun showError() {
        viewHandler.showView(R.id.error)
    }

    @Suppress("UNCHECKED_CAST")
    override fun showContent(detailBean: ProductDetailBean) {
        val contentView = viewHandler.showView(R.id.content)?:return

        val priceSignStr = SysContext.getString(R.string.priceSign)

        val list = detailBean.bannerArr.toList()
        val bannerView: ConvenientBanner<String> = contentView.bannerView as ConvenientBanner<String>
        bannerView.setPages(BannerCreator(), list)
        bannerView.isCanLoop = true

        contentView.bannerCountTxt.setTag(R.id.data, list.size)
        contentView.titleTxt.text = detailBean.title
        when(detailBean.sourceType) {
            SourceType.TAOBAO -> {
                contentView.brandTxt.visibility = View.VISIBLE
                contentView.brandTxt.text = "淘宝"
                contentView.brandTxt.setTextColor(SysContext.getColor(R.color.whiteColor))
                contentView.brandTxt.setBackgroundResource(R.drawable.bg_brand_taobao)

                contentView.orgPriceTxt.text = "淘宝价 $priceSignStr${StringUtils.formatPrice(detailBean.orgPrice)}"
            }
            SourceType.TMALL -> {
                contentView.brandTxt.visibility = View.VISIBLE
                contentView.brandTxt.text = "天猫"
                contentView.brandTxt.setTextColor(SysContext.getColor(R.color.redColor))
                contentView.brandTxt.setBackgroundResource(R.drawable.bg_brand_tmall)

                contentView.orgPriceTxt.text = "天猫价 $priceSignStr${StringUtils.formatPrice(detailBean.orgPrice)}"
            }
            else -> {
                contentView.brandTxt.visibility = View.GONE
                contentView.orgPriceTxt.text = "原价 $priceSignStr${StringUtils.formatPrice(detailBean.orgPrice)}"
            }
        }

        contentView.saleCountTxt.text = "已抢${StringUtils.formatCount(detailBean.saleCount)}件"

        contentView.priceTipsTxt.text = "${detailBean.priceTip}$priceSignStr"
        contentView.priceTxt.text = StringUtils.buildBigPrice(price = detailBean.afterRewardPrice, size = SysContext.getResourceSp(R.dimen.text_price).toInt())

        if(detailBean.isExistReward()) {
            contentView.rewardTipsTxt.visibility = View.VISIBLE
            contentView.rewardTxt.visibility = View.VISIBLE
            contentView.rewardHelpBtn.visibility = View.VISIBLE

            contentView.rewardTxt.text = "返${StringUtils.formatPrice(detailBean.rewardPrice)}"
        }
        else {
            contentView.rewardTipsTxt.visibility = View.GONE
            contentView.rewardTxt.visibility = View.GONE
            contentView.rewardHelpBtn.visibility = View.GONE
        }

        if(detailBean.isExistCoupon()) {
            contentView.couponContainer.visibility = View.VISIBLE
            contentView.couponPriceTxt.text = "${StringUtils.formatPrice(detailBean.couponPrice)}元优惠券"
            contentView.couponTimeTxt.text = "使用期限: ${detailBean.couponTimeStr}"
        }
        else contentView.couponContainer.visibility = View.GONE


        if(detailBean.isExistShop()) {
            contentView.shopSplitLine.visibility = View.VISIBLE
            contentView.shopContainer.visibility = View.VISIBLE

            if(detailBean.shopIcon.isNotBlank()) {
                contentView.shopIconImg.visibility = View.VISIBLE
                SysContext.getGlide().loadNetPicDirectly(detailBean.shopIcon, R.drawable.bg_default, contentView.shopIconImg)
            }
            else contentView.shopIconImg.visibility = View.GONE

            contentView.shopTitleTxt.text = detailBean.shopTitle

            contentView.goodDescScoreTxt.text = "宝贝描述:${detailBean.goodDescScore}"
            contentView.sellerScoreTxt.text = "卖家描述:${detailBean.sellerScore}"
            contentView.postScoreTxt.text = "物流描述:${detailBean.postScore}"
        }
        else {
            contentView.shopSplitLine.visibility = View.GONE
            contentView.shopContainer.visibility = View.GONE
        }

        if(detailBean.isExistDetail()) {
            contentView.detailSplitLine.visibility = View.VISIBLE
            contentView.detailTitleTxt.visibility = View.VISIBLE
            contentView.detailContainer.visibility = View.VISIBLE

            detailBean.descDetail!!.forEach {
                val imgView: ImageView = ViewUtils.inflateView(contentView.detailContainer, R.layout.view_detail_image)
                imgView.setTag(R.id.data, it)
                contentView.detailContainer.addView(imgView)
            }
        }
        else {
            contentView.detailSplitLine.visibility = View.GONE
            contentView.detailTitleTxt.visibility = View.GONE
            contentView.detailContainer.visibility = View.GONE
        }

        contentView.botOrgPriceTxt.text = StringUtils.formatPrice(detailBean.orgPrice, priceSignStr)
        contentView.buyTxt.text = StringUtils.formatPrice(detailBean.price, priceSignStr)

        processBannerChanged(0)
        processScrollY(0)
    }

    override fun openBuyPage(convertLink: String?, productId: String, pid: String?) {
        if(convertLink != null) {
            val taokeParams = AlibcTaokeParams()
            taokeParams.pid = pid
            AlibcTrade.show(this, AlibcPage(convertLink), AlibcShowParams(OpenType.Native, false), taokeParams, null, object: AlibcTradeCallback {
                override fun onFailure(p0: Int, p1: String?) {

                }

                override fun onTradeSuccess(p0: AlibcTradeResult?) {
                }
            })
        }
        else {
            val taokeParams = AlibcTaokeParams()
            taokeParams.pid = pid
            AlibcTrade.show(this, AlibcDetailPage(productId), AlibcShowParams(OpenType.Native, false), taokeParams, null, object: AlibcTradeCallback {
                override fun onFailure(p0: Int, p1: String?) {

                }

                override fun onTradeSuccess(p0: AlibcTradeResult?) {
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AlibcTradeSDK.destory()
    }

    private fun processScrollY(scrollY: Int) {
        val contentView = viewHandler.showView(R.id.content)?:return

        if(topGuide.tag != true) {
            if (scrollY < TOP_ANIMATION_LENGTH) {
                val ratio = scrollY * 1f / TOP_ANIMATION_LENGTH
                topGuide.setBackgroundColor(argbEvaluator.evaluate(ratio, Color.TRANSPARENT, Color.BLACK) as Int)
            } else topGuide.setBackgroundColor(Color.BLACK)
        }

        if(contentView.detailContainer.visibility == View.VISIBLE && contentView.detailContainer.height != 0) {
            val detailContainer = contentView.detailContainer
            val distance = scrollY + contentView.scrollView.height - detailContainer.top
            if (distance > 0) {
                detailContainer.forEach {
                    child->
                    if (child is OnceImageView) {
                        if(child.isLoading || child.isSuccess)
                            return@forEach

                        SysContext.getGlide().loadNetPicDirectly(
                            child.getTag(R.id.data) as String,
                            R.drawable.bg_default,
                            OnceImageView.Target(child)
                        )
                    }
                }
            }
        }
    }

    private fun processBannerChanged(index: Int) {
        val contentView = viewHandler.showView(R.id.content)?:return

        val bannerCountTxt = contentView.bannerCountTxt
        val totalCount = bannerCountTxt.getTag(R.id.data) as Int
        bannerCountTxt.text = "${index + 1} / $totalCount"
    }




}
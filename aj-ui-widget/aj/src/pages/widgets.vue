
<template>
  <div class="home">
<!--     <h2>图片放大器</h2>
    <div style="margin:0 auto;width:600px;text-align: center;">
      <ImageEnlarger img-url="https://thebulletin.org/wp-content/uploads/2021/11/rosner-photo-1-150x150.jpg.webp" />
      &nbsp;&nbsp;
      <ImageEnlarger img-url="https://thebulletin.org/wp-content/uploads/2021/11/1280px-Construction_at_Vogtle_Nuclear_Plant-150x150.jpeg.webp" />
    </div> -->

    <h2>展开闭合器 expander</h2>
    <p>常用于展开正文详情。提供过渡效果的图层，如果不需要把 div class="mask" 去掉即可。注意加入容器的 padding 会导致关闭时高度异常。</p>
    <table class="aj-table" align="center">
      <thead>
        <tr>
          <th>属性</th>
          <th>含义</th>
          <th>类型</th>
          <th>是否必填，默认值</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>openHeight</td>
          <td>展开状态的高度</td>
          <td>Number</td>
          <td>n, 200</td>
        </tr>
        <tr>
          <td>closeHeight</td>
          <td>闭合状态的高度</td>
          <td>Number</td>
          <td>n, 50</td>
        </tr>
        <tr>
          <td><code>&lt;slot</code>&gt;</td>
          <td>正文内容扩展</td>
          <td>String</td>
          <td>n</td>
        </tr>
      </tbody>
    </table>

    <Expander :close-height="50" style="width: 300px;margin:5% auto;">
      Lorem Ipsum，也称乱数假文或者哑元文本， 是印刷及排版领域所常用的虚拟文字。
      由于曾经一台匿名的打印机刻意打乱了一盒印刷字体从而造出一本字体样品书， Lorem Ipsum从西元15世纪起就被作为此领域的标准文本使用。
      <div class="mask-layer"></div>
    </Expander>

    <h2>渐现 Banner</h2>
    <p>原理上讲，就是为每张图片准备好定时器 timer，使其控制图片的透明度。当图片从透明度 0 到 100，就是渐现的过程； 与此同时，另外一张图片由透明度 100 下降到 0。这两个过程是同时发生的，一个渐现一个渐隐，便会造成如此的目标效果。</p>
    <p><a href="https://zhangxin.blog.csdn.net/article/details/112055111" target="_blank">查看教程</a>
    </p>

    <OpacityBanner style="width:300px;margin:0 auto">
      <li><img src="https://img1.mydrivers.com/img/topimg/20220315/192429144.jpg" /></li>
      <li><img src="https://img1.mydrivers.com/img/topimg/20220315/192403940.jpg" /></li>
      <li><img src="https://img1.mydrivers.com/img/topimg/20220315/192314256.jpg" /></li>
      <li><a target="iframepage" href="../job/job.jsp">招聘管理</a></li>
    </OpacityBanner>

    <br />
    <br />
    <br />
    <br />
    <h2>进度条 Process Line</h2>
    <ProcessLine />

    <h2>折叠菜单 Accordion Menu</h2>
    <p>折叠菜单的特点是同一时间只展开一个区域，其他区域则处于闭合状态。</p>

    <p>属性说明：</p>
    <table class="aj-table" align="center" width="500">
      <thead>
        <tr>
          <th>属性</th>
          <th>含义</th>
          <th>类型</th>
          <th>是否必填，默认值</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td><code>&lt;slot&gt;</code></td>
          <td>菜单内容</td>
          <td>String</td>
          <td>y</td>
        </tr>
      </tbody>
    </table>
    <br />
    <br />
    <br />
    <AccordionMenu style="width:300px;margin:0 auto">
      <li class="siteMgr">
        <h3>网站管理</h3>
        <ul>
          <li><a target="iframepage" href="website/profile/changePassword.jsp">帐号信息</a></li>
          <li><a target="iframepage" href="website/profile/companyInfo.jsp">公司信息</a></li>
          <li><a target="iframepage" href="website/gallery/companyAlbum.jsp">相 册</a></li>
          <li><a target="iframepage" href="website/pageEditor/index.jsp">页面维护</a></li>
          <li><a target="iframepage" href="website/profile/global.jsp">全局信息</a></li>
        </ul>
      </li>

      <li class="news">
        <h3>新闻中心管理</h3>
        <ul>
          <li><a target="iframepage" href="entry/list/news">新闻管理</a></li>
        </ul>
      </li>
      <li class="levelPoints">
        <h3>积分系统</h3>
        <ul>
          <li><a target="iframepage" href="levelPoints/memberTree.jsp">会员下线一览</a>
          </li>
          <li><a target="iframepage" href="levelPoints/showUserPoints.jsp">显示会员积分</a></li>
        </ul>
      </li>
      <li>
        <h3>其他信息</h3>
        <ul>
          <li><a target="iframepage" href="../job/job.jsp">留言管理</a></li>
          <li><a target="iframepage" href="../job/job.jsp">招聘管理</a></li>
        </ul>
      </li>
    </AccordionMenu>

    <br />
    <br />
    <br />

    <h2>Baidu 自定义搜索</h2>
    <table class="aj-table" align="center">
      <thead>
        <tr>
          <th>属性</th>
          <th>含义</th>
          <th>类型</th>
          <th>是否必填，默认值</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>siteDomainName</td>
          <td>搜索网站之域名</td>
          <td>String</td>
          <td>n, 通过 js 返回域名</td>
        </tr>
      </tbody>
    </table>
    <br />
    <br />
    <div style="margin:0 auto;width:600px;">
      <BaiduSearch />
    </div>

    <br />
    <br />
  </div>
</template>

<script>
import OpacityBanner from "@/widget/OpacityBanner.vue";
import ProcessLine from "@/widget/ProcessLine.vue";
import AccordionMenu from "@/widget/AccordionMenu.vue";
import Expander from "@/widget/Expander.vue";
import BaiduSearch from "@/widget/BaiduSearch.vue";
import ImageEnlarger from "@/widget/ImageEnlarger.vue";

export default {
  components: {
    OpacityBanner,
    ProcessLine,
    AccordionMenu,
    Expander,
    BaiduSearch,
    ImageEnlarger,
  },
};
</script>

<style lang="less" scoped >
p {
  width: 500px;
  // .aj-text-function();
  margin: 20px auto;
  text-indent: 2em;
}
h2 {
  // text-align: left;
  margin: 20px auto;
}
</style>

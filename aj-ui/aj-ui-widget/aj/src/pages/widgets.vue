
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

    <h2>文章 Article</h2>
        <Article title="新股上市首日破发并非坏事 警示投资者打新风险" author="张三" source="http://qq.com" style="max-width:700px;margin:0 auto">

      <p class="one-p">10月22日是科创板新股<span class="linkNormal">中自科技</span>新股上市的日子。按照惯例，新股上市大都会给打新中签的投资者带来一笔无风险的“打新收益”。但对于中自科技的中签者来说，他们期待中的“打新收益”落空了，迎接他们的是新股上市首日破发的命运。打新中签者不仅没有得到“打新收益”，相反还要承担一定的“打新损失”，中自科技上市首日的收盘价较发行价下跌了6.87%。</p>
      <p class="one-p">新股上市首日破发，这对于打新中签者来说是非常揪心的一件事情。尤其是中自科技开盘即破发，没有给中签者丝毫的逃命机会。当天的最高价是70.50元，低于70.90元的发行价，这意味着打新中签者悉数被套，全部都要为自己“中签”的好运而付出代价。</p>
      <p class="one-p">那么，中自科技为何会出现上市首日破发现象呢？究其原因主要有两点。一是发行价偏高，高达每股70.90元，其动态市盈率达到百倍。二是今年该公司业绩大幅滑坡。该公司预计，2021年1~9月营业收入为8.6亿元~9.5亿元，同比下滑57.48%至53.03%；预计归母净利润为4500万元~5400万元，同比下滑75.33%至70.40%；预计扣非后归母净利润为3000万元~3900万元，同比下滑84.63%至80.02%。也正因如此，中自科技上市首日破发并不令人意外，这也进一步证实投资者打新之盲目了。</p>
      <p class="one-p"><img class="content-picture" src="https://img1.mydrivers.com/img/20210706/S91792917-e725-484b-b8c9-b2d0eda32981.png">
      </p>
      <p class="one-p">当然，新股上市首日破发在A股市场还是比较少见的。中自科技是今年以来科创板第一家上市首日破发新股，也是科创板成立以来第二只上市首日破发新股。科创板第一只上市首日破发新股是<span class="linkNormal">建龙微纳</span>，其上市之日是2019年12月4日，距今已有一年零10个月，这也足见新股上市首日破发的现象在A股市场并不常见。</p>
      <p class="one-p">实际上，对于A股市场来说，新股上市首日破发是一件好事。虽然对于A股市场来说，新股破发现象并不少见，但这种新股破发大多都是新股上市之后经过了一轮甚至是几番的市场炒作之后，然后股价回落破发。这样的新股破发除了提醒投资者注意市场炒作的风险之外，其意义并不大。相反，新股上市首日破发意义更加重大。</p>
      <p class="one-p">首先，新股上市首日破发它有利于提醒询价机构理性询价，而不能为了得到新股份额一味报高价。因为参与新股询价的机构投资者在询价有效的情况下，也是要参与新股配售的。如果新股定价太高，上市就破发了，这对于询价机构来说当然也是不利的。因为他们参与的新股配售份额也会因为破发而出现浮亏。</p>
      <p class="one-p">其次，新股上市首日破发也有利于向投资者提醒打新的风险。因为A股市场长期以来的新股不败现象，结果导致投资者在打新时逢新必打，根本就不关心新股的基本面，同时也不关心新股的发行价格，甚至将高价发行的新股视为“肉签”股。虽然在过去也经常出现新股破发现象，但新股经过一番炒作之后的破发，并没有危及到打新者的利益，并不会让打新者增加风险意识。而只有新股上市首日破发，才会直接损害到打新者的利益，只有在这种情况下，打新者才会真正意识到打新的风险，从而让打新者关心到新股的基本面，学会理性打新。当然，要达到这个目的，还需要新股上市首日破发现象进一步增加，呈现出一种常态化，这样更有利于投资者理性打新。</p>
      <p class="one-p">此外，新股上市首日破发也有利于向市场警示炒新的风险。在新股不败的市场环境下，投资者除了盲目打新之外，还会盲目炒新，以至一只新股上市后，通常都会炒出几个、十几个甚至几十个涨停板出来。因此，一只新股上市，往往都会受到市场的投机炒作。而新股上市首日破发，对于炒新者是一个打击。因为炒新者盲目追进新股之后发现，股价不仅没有炒上去，相反股价在进一步下跌，炒新变成了套牢。因此，新股上市首日破发对于炒新者来说也是一种风险警示。</p>

    </Article>
  </div>
</template>

<script>
import OpacityBanner from "@/widget/OpacityBanner.vue";
import ProcessLine from "@/widget/ProcessLine.vue";
import AccordionMenu from "@/widget/AccordionMenu.vue";
import Expander from "@/widget/Expander.vue";
import BaiduSearch from "@/widget/BaiduSearch.vue";
import ImageEnlarger from "@/widget/ImageEnlarger.vue";
import Article from '@/widget/Article.vue';

export default {
  components: {
    OpacityBanner,
    ProcessLine,
    AccordionMenu,
    Expander,
    BaiduSearch,
    ImageEnlarger,
    Article
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

<%@tag	pageEncoding="UTF-8" description="省市地控件"%>
<!-- 出发地控件 -->
<textarea class="fromLocationTpl hide">
    <span class="cityChoserByInput">
        <input type="text" requiredField tipsMsg="请从下方选择地点" readonly placeholder="" value="广东-广州" />

        <input type="hidden" name="fromProvinceId" value="78350" />
        <input type="hidden" name="fromProvince" value="广东" />
        <input type="hidden" name="fromAreaId" value="78365" />
        <input type="hidden" name="fromArea" value="广州" />
        <input type="hidden" name="fromCountyId" value="" />
        <input type="hidden" name="fromCounty" value="" />

        <!-- 选择省市区控件 -->
        <div class="newForm cityChoserHolder">
            <div class="newForm cityChoser tab2">
                <ul>
                   <li class="selected">省&nbsp;<span class="arrow"></span></li>
                   <li>市&nbsp;<span class="arrow"></span></li>
                   <li>区县&nbsp;<span class="arrow"></span></li>
                </ul>
                <div class="content">
                    <div class="tab provinceSel"></div>
                    <div class="tab areaSel" style="display:none;"></div>
                    <div class="tab countySel" style="display:none;"></div>
                </div>
            </div>
        </div>
        <!-- // 选择省市区控件 -->
    </span>
</textarea>
<!-- // 出发地控件 -->

<!-- 到达地控件 -->
<textarea class="toLocationTpl hide">
    <span class="cityChoserByInput">
        <input type="text" requiredField tipsMsg="请从下方选择地点" readonly placeholder="请从下方选择地点" />

        <input type="hidden" name="toProvinceId" value="" />
        <input type="hidden" name="toProvince" value="" />
        <input type="hidden" name="toAreaId" value="" />
        <input type="hidden" name="toArea" value="" />
        <input type="hidden" name="toCountyId" value="" />
        <input type="hidden" name="toCounty" value="" />

        <!-- 选择省市区控件 -->
        <div class="newForm cityChoserHolder">
            <div class="newForm cityChoser tab2">
                <ul>
                   <li class="selected">省&nbsp;<span class="arrow"></span></li>
                   <li>市&nbsp;<span class="arrow"></span></li>
                   <li>区县&nbsp;<span class="arrow"></span></li>
                </ul>
                <div class="content">
                    <div class="tab provinceSel"></div>
                    <div class="tab areaSel" style="display:none;"></div>
                    <div class="tab countySel" style="display:none;"></div>
                </div>
            </div>
        </div>
        <!-- // 选择省市区控件 -->
    </span>
</textarea>
<!-- //到达地控件 -->  
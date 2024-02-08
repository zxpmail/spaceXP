import{r as w,a as L,b as i,o as x,c as S,w as t,d as v,e,j as $,E as F,i as D,u as m,k as I}from"./index-IC1vEKgd.js";import{u as A}from"./index-E_Ll_SfN.js";import{c as H,d as R,u as O}from"./project-jONgqCzb.js";const E={__name:"add-or-update",emits:["refreshDataList"],setup(q,{expose:s,emit:u}){const V=u,g=w(!1),_=w(),o=L({id:"",artifactId:"test",groupId:"cn.piesat",version:"1.0.0",type:"单体",springDoc:1,bootVersion:1,author:"zhouxiaoping",email:"zhouxiaoping@piesat.cn",description:"",port:8080}),U=p=>{g.value=!0,o.id="",_.value&&_.value.resetFields(),p&&h(p)},h=p=>{H(p).then(l=>{Object.assign(o,l.data)})},z=w({artifactId:[{required:!0,message:"必填项不能为空",trigger:"blur"}],groupId:[{required:!0,message:"必填项不能为空",trigger:"blur"}],version:[{required:!0,message:"必填项不能为空",trigger:"blur"}],type:[{required:!0,message:"必填项不能为空",trigger:"blur"}],springDoc:[{required:!0,message:"必填项不能为空",trigger:"change"}]}),k=()=>{_.value.validate(p=>{if(!p)return!1;R(o).then(()=>{F.success({message:"操作成功",duration:500,onClose:()=>{g.value=!1,V("refreshDataList")}})})})};return s({init:U}),(p,l)=>{const n=i("el-input"),d=i("el-form-item"),r=i("el-option"),C=i("el-select"),c=i("el-form"),y=i("el-button"),j=i("el-dialog");return x(),S(j,{modelValue:g.value,"onUpdate:modelValue":l[13]||(l[13]=a=>g.value=a),title:o.id?"修改":"新增","close-on-click-modal":!1},{footer:t(()=>[e(y,{onClick:l[11]||(l[11]=a=>g.value=!1)},{default:t(()=>[v("取消")]),_:1}),e(y,{type:"primary",onClick:l[12]||(l[12]=a=>k())},{default:t(()=>[v("确定")]),_:1})]),default:t(()=>[e(c,{ref_key:"dataFormRef",ref:_,model:o,rules:z.value,"label-width":"100px",onKeyup:l[10]||(l[10]=$(a=>k(),["enter"]))},{default:t(()=>[e(d,{label:"项目标识",prop:"artifactId"},{default:t(()=>[e(n,{modelValue:o.artifactId,"onUpdate:modelValue":l[0]||(l[0]=a=>o.artifactId=a),placeholder:"项目标识"},null,8,["modelValue"])]),_:1}),e(d,{label:"项目包名",prop:"groupId"},{default:t(()=>[e(n,{modelValue:o.groupId,"onUpdate:modelValue":l[1]||(l[1]=a=>o.groupId=a),placeholder:"项目包名"},null,8,["modelValue"])]),_:1}),e(d,{label:"版本号",prop:"version"},{default:t(()=>[e(n,{modelValue:o.version,"onUpdate:modelValue":l[2]||(l[2]=a=>o.version=a),placeholder:"版本号"},null,8,["modelValue"])]),_:1}),e(d,{label:"项目类型",prop:"type"},{default:t(()=>[e(C,{modelValue:o.type,"onUpdate:modelValue":l[3]||(l[3]=a=>o.type=a),clearable:"",placeholder:"项目类型",style:{width:"100%"}},{default:t(()=>[e(r,{value:"单体",label:"单体"}),e(r,{value:"EUREKA",label:"EUREKA"}),e(r,{value:"NACOS",label:"NACOS"}),e(r,{value:"NACOS CONFIG",label:"NACOS CONFIG"}),e(r,{value:"代码",label:"代码"})]),_:1},8,["modelValue"])]),_:1}),e(d,{label:"组件版本",prop:"bootVersion"},{default:t(()=>[e(C,{modelValue:o.bootVersion,"onUpdate:modelValue":l[4]||(l[4]=a=>o.bootVersion=a),clearable:"",placeholder:"组件版本",style:{width:"100%"}},{default:t(()=>[e(r,{value:0,label:"2.0.0"}),e(r,{value:1,label:"3.0.0"})]),_:1},8,["modelValue"])]),_:1}),e(d,{label:"文档方式",prop:"springDoc"},{default:t(()=>[e(C,{modelValue:o.springDoc,"onUpdate:modelValue":l[5]||(l[5]=a=>o.springDoc=a),clearable:"",placeholder:"项目类型",style:{width:"100%"}},{default:t(()=>[e(r,{value:1,label:"springDoc"}),e(r,{value:0,label:"springFox"})]),_:1},8,["modelValue"])]),_:1}),e(d,{label:"作者",prop:"author"},{default:t(()=>[e(n,{modelValue:o.author,"onUpdate:modelValue":l[6]||(l[6]=a=>o.author=a),placeholder:"作者"},null,8,["modelValue"])]),_:1}),e(d,{label:"EMail",prop:"email"},{default:t(()=>[e(n,{modelValue:o.email,"onUpdate:modelValue":l[7]||(l[7]=a=>o.email=a),placeholder:"EMail"},null,8,["modelValue"])]),_:1}),e(d,{label:"项目端口",prop:"port"},{default:t(()=>[e(n,{modelValue:o.port,"onUpdate:modelValue":l[8]||(l[8]=a=>o.port=a),placeholder:"项目端口"},null,8,["modelValue"])]),_:1}),e(d,{label:"项目描述",prop:"port"},{default:t(()=>[e(n,{modelValue:o.description,"onUpdate:modelValue":l[9]||(l[9]=a=>o.description=a),type:"textarea",placeholder:"项目描述"},null,8,["modelValue"])]),_:1})]),_:1},8,["model","rules"])]),_:1},8,["modelValue","title"])}}},K={__name:"download",setup(q,{expose:s}){const u=L({dataListUrl:"/table/list",queryForm:{connName:""}}),V=w(!1);w();const g=w(""),_=L({project:{}}),o=l=>{V.value=!0,_.project=l,h()},U=()=>{const l=u.dataListSelections?u.dataListSelections:[];if(l.length===0){F.warning("请选择生成代码的表");return}const n=u.dataList.filter(r=>l.includes(r.id));if(Array.from(new Set(n.map(({datasourceName:r})=>r))).length>1){F.warning("生成代码必须保证为相同的数据源名称");return}_.project.tables=n,_.project.tablePrefix=g.value,O("generator/genProjectCode",_.project),V.value=!1};s({init:o});const{getDataList:h,selectionChangeHandle:z,sizeChangeHandle:k,currentChangeHandle:p}=A(u);return(l,n)=>{const d=i("el-input"),r=i("el-form-item"),C=i("el-button"),c=i("el-form"),y=i("el-table-column"),j=i("el-table"),a=i("el-pagination"),N=i("el-dialog"),b=D("loading");return x(),S(N,{modelValue:V.value,"onUpdate:modelValue":n[6]||(n[6]=f=>V.value=f),title:"源码下载","close-on-click-modal":!1},{footer:t(()=>[e(C,{onClick:n[4]||(n[4]=f=>V.value=!1)},{default:t(()=>[v("取消")]),_:1}),e(C,{type:"primary",onClick:n[5]||(n[5]=f=>U())},{default:t(()=>[v("下载")]),_:1})]),default:t(()=>[e(c,{inline:!0,model:u.queryForm,onKeyup:n[3]||(n[3]=$(f=>m(h)(),["enter"]))},{default:t(()=>[e(r,null,{default:t(()=>[e(d,{modelValue:u.queryForm.connName,"onUpdate:modelValue":n[0]||(n[0]=f=>u.queryForm.connName=f),placeholder:"数据源名称"},null,8,["modelValue"])]),_:1}),e(r,null,{default:t(()=>[e(C,{onClick:n[1]||(n[1]=f=>m(h)())},{default:t(()=>[v("查询")]),_:1})]),_:1}),e(r,null,{default:t(()=>[e(d,{modelValue:g.value,"onUpdate:modelValue":n[2]||(n[2]=f=>g.value=f),placeholder:"表前缀忽略项"},null,8,["modelValue"])]),_:1})]),_:1},8,["model"]),I((x(),S(j,{data:u.dataList,border:"",style:{width:"100%"},onSelectionChange:m(z)},{default:t(()=>[e(y,{type:"selection","header-align":"center",align:"center",width:"50"}),e(y,{prop:"tableName",label:"表名","header-align":"center",align:"center"}),e(y,{prop:"tableComment",label:"表说明","header-align":"center",align:"center"}),e(y,{prop:"connName",label:"数据源名称","header-align":"center",align:"center"})]),_:1},8,["data","onSelectionChange"])),[[b,u.dataListLoading]]),e(a,{"current-page":u.page,"page-sizes":u.pageSizes,"page-size":u.limit,total:u.total,layout:"total, sizes, prev, pager, next, jumper",onSizeChange:m(k),onCurrentChange:m(p)},null,8,["current-page","page-sizes","page-size","total","onSizeChange","onCurrentChange"])]),_:1},8,["modelValue"])}}},G={__name:"index",setup(q){const s=L({dataListUrl:"/project/list",deleteUrl:"/project/delete",queryForm:{projectName:""}}),u=w(),V=p=>{u.value.init(p)},g=w(),_=p=>{g.value.init(p)},{getDataList:o,selectionChangeHandle:U,sizeChangeHandle:h,currentChangeHandle:z,deleteBatchHandle:k}=A(s);return(p,l)=>{const n=i("el-input"),d=i("el-form-item"),r=i("el-button"),C=i("el-form"),c=i("el-table-column"),y=i("el-table"),j=i("el-pagination"),a=i("el-card"),N=D("loading");return x(),S(a,null,{default:t(()=>[e(C,{inline:!0,model:s.queryForm,onKeyup:l[4]||(l[4]=$(b=>m(o)(),["enter"]))},{default:t(()=>[e(d,null,{default:t(()=>[e(n,{modelValue:s.queryForm.projectName,"onUpdate:modelValue":l[0]||(l[0]=b=>s.queryForm.projectName=b),placeholder:"项目名"},null,8,["modelValue"])]),_:1}),e(d,null,{default:t(()=>[e(r,{onClick:l[1]||(l[1]=b=>m(o)())},{default:t(()=>[v("查询")]),_:1})]),_:1}),e(d,null,{default:t(()=>[e(r,{type:"primary",onClick:l[2]||(l[2]=b=>V())},{default:t(()=>[v("新增")]),_:1})]),_:1}),e(d,null,{default:t(()=>[e(r,{type:"danger",onClick:l[3]||(l[3]=b=>m(k)())},{default:t(()=>[v("删除")]),_:1})]),_:1})]),_:1},8,["model"]),I((x(),S(y,{data:s.dataList,border:"",style:{width:"100%"},onSelectionChange:m(U)},{default:t(()=>[e(c,{type:"selection","header-align":"center",align:"center",width:"50"}),e(c,{prop:"artifactId",label:"项目标识","header-align":"center",align:"center"}),e(c,{prop:"groupId",label:"项目包名","show-overflow-tooltip":"","header-align":"center",align:"center"}),e(c,{prop:"version",label:"版本","show-overflow-tooltip":"","header-align":"center",align:"center"}),e(c,{prop:"type",label:"类型","show-overflow-tooltip":"","header-align":"center",align:"center"}),e(c,{prop:"author",label:"作者","show-overflow-tooltip":"","header-align":"center",align:"center"}),e(c,{prop:"email",label:"Email","show-overflow-tooltip":"","header-align":"center",align:"center"}),e(c,{label:"操作",fixed:"right","header-align":"center",align:"center",width:"180"},{default:t(b=>[e(r,{type:"primary",link:"",onClick:f=>V(b.row.id)},{default:t(()=>[v("修改")]),_:2},1032,["onClick"]),e(r,{type:"primary",link:"",onClick:f=>_(b.row)},{default:t(()=>[v("源码下载")]),_:2},1032,["onClick"]),e(r,{type:"primary",link:"",onClick:f=>m(k)(b.row.id)},{default:t(()=>[v("删除")]),_:2},1032,["onClick"])]),_:1})]),_:1},8,["data","onSelectionChange"])),[[N,s.dataListLoading]]),e(j,{"current-page":s.page,"page-sizes":s.pageSizes,"page-size":s.size,total:s.total,layout:"total, sizes, prev, pager, next, jumper",onSizeChange:m(h),onCurrentChange:m(z)},null,8,["current-page","page-sizes","page-size","total","onSizeChange","onCurrentChange"]),e(E,{ref_key:"addOrUpdateRef",ref:u,onRefreshDataList:m(o)},null,8,["onRefreshDataList"]),e(K,{ref_key:"downloadRef",ref:g},null,512)]),_:1})}}};export{G as default};

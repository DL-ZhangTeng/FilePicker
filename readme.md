
# 文件筛选库（按照某固定类型筛选）

FilePicker是一个Android版本的文件筛选库。
[GitHub仓库地址](https://github.com/duoluo9/FilePicker)
## 引入
### gradle
```groovy
allprojects {
    repositories {
        maven { url 'https://dl.bintray.com/duoluo9/FilePicker' }
    }
}

implementation 'com.zhangteng.searchfilelibrary:filepicker-searchfilelibrary:1.0.1'
//UI 公共库
implementation 'com.zhangteng.common:filepicker-common:1.0.1'
//压缩包UI
implementation 'com.zhangteng.rarpicker:filepicker-rarpicker:1.0.1'
//文件夹UI
implementation 'com.zhangteng.folderpicker:filepicker-folderpicker:1.0.1'
//图片UI
implementation 'com.zhangteng.imagepicker:filepicker-imagepicker:1.0.1'
//视频UI
implementation 'com.zhangteng.videopicker:filepicker-videopicker:1.0.1'
//音频UI
implementation 'com.zhangteng.audiopicker:filepicker-audiopicker:1.0.1'
//文档UI
implementation 'com.zhangteng.documentpicker:filepicker-documentpicker:1.0.1'
```

## 效果图
![图片选择UI](https://img-blog.csdnimg.cn/20200914093052474.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2R1b2x1bzk=,size_16,color_FFFFFF,t_70#pic_center)
![RAR选择UI](https://img-blog.csdnimg.cn/20200914093222160.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2R1b2x1bzk=,size_16,color_FFFFFF,t_70#pic_center)
![文件夹选择UI](https://img-blog.csdnimg.cn/20200914093256824.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2R1b2x1bzk=,size_16,color_FFFFFF,t_70#pic_center)
## 检索结果
获取检索结果提供了2种方式（具体可参照GitHub源码）
1、  public void getMediaList(int fileModel, Context context)
     查找结果放于MediaStoreUtil相应的字段内；
     MediaStoreUtil提供了数据改变监听解决办法，通过观察者模式向所有注册了MediaStoreListener的对象发送数据改变结果，
     添加数据监听
     public static void setListener(MediaStoreListener listener)
     移除数据监听
     public static void removeListener(int index)
     public static void removeListener(MediaStoreListener listener)

2、  public void getMediaList(int searchId, Handler handler)
     查找结果交给handler处理
     Message message = new Message();
     message.what = GetListCallbak.SUCCESS;
     message.obj = list;
     handler.sendMessage(message);

使用方法：开启服务根据需要调用服务的相应public方法





## UI属性
属性名| 描述
--- | -----
multiSelect| 是否单选，默认true
maxSize| 配置开启多选时 最大可选择的图片数量。   默认：9
isShowCamera| 是否在第一格显示拍照或录制，默认true（只有图片视频音频有效）
filePath| 拍照以及截图后 存放的位置。    默认：/filePicker/FilePicker
provider| 文件提供者，默认com.zhangteng.searchfilelibrary.fileprovider
pathList| 已选择照片的路径
isOpenCamera| 是否直接开启相机或录制    默认：false（只有图片视频音频有效）
iconResources|icon资源（可设置每种类型的icon，文档类型中pdf、txt、word、excel等可单独设置）

## 使用
```java
  //使用UI(直接使用UI中的Fragment)
  public class FilePickerAdapter extends FragmentPagerAdapter {
    private String[] titles = {"image", "video", "audio", "rar", "document", "folder"};
    private ArrayList<Fragment> fragmentlist = new ArrayList<Fragment>();

    public FilePickerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ArrayList<Fragment> getFragmentlist() {
        return fragmentlist;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ImagePickerFragment();
                break;
            case 1:
                fragment = new VideoPickerFragment();
                break;
            case 2:
                fragment = new AudioPickerFragment();
                break;
            case 3:
                fragment = new RarPickerFragment();
                break;
            case 4:
                fragment = new DocumentPickerFragment();
                break;
            case 5:
                fragment = new FolderPickerFragment();
                break;
            default:
                break;
        }
        fragmentlist.add(fragment);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}

  TabLayout myTabLayout = findViewById(R.id.mytablayout);
  ViewPager viewPager = findViewById(R.id.viewpager);
  viewPager.setOffscreenPageLimit(6);
  FilePickerAdapter filePickerAdapter = new FilePickerAdapter(getSupportFragmentManager());
  viewPager.setAdapter(filePickerAdapter);
  myTabLayout.setupWithViewPager(viewPager);
  //使用UI(直接使用UI中的Activity)
  startActivity(new Intent(this, ImagePickerActivity.class));
  startActivity(new Intent(this, VideoPickerActivity.class));
  startActivity(new Intent(this, AudioPickerActivity.class));
  startActivity(new Intent(this, RarPickerActivity.class));
  startActivity(new Intent(this, DocumentPickerActivity.class));
  startActivity(new Intent(this, FolderPickerActivity.class));
  //不使用UI
  getActivity().startService(new Intent(getActivity(), FileService.class));
  FileService.getInstance().getFileList(null);
  //FileService.getInstance().getMediaList(MediaEntity.MEDIA_AUDIO, getActivity());
  MediaStoreUtil.setListener(new MediaStoreUtil.AudioListener() {

      @Override
      public void onAudioChange(int imageCount, List<MediaEntity> folders) {
          imageInfos.clear();
          imageInfos.addAll(folders);
          if (getActivity() == null) {
              return;
          }
          getActivity().runOnUiThread(() -> folderPickerAdapter.notifyDataSetChanged());
      }
  });
  //使用UI时-文件选择回调
  public class HandlerCallBack implements IHandlerCallBack {
    private String TAG = "---ImagePicker---";

    @Override
    public void onStart() {
        Log.i(TAG, "onStart: 开启");
    }

    @Override
    public void onSuccess(List<String> audioList) {
        Log.i(TAG, "onSuccess: 返回数据");
    }

    @Override
    public void onCancel() {
        Log.i(TAG, "onCancel: 取消");
    }

    @Override
    public void onFinish() {
        Log.i(TAG, "onFinish: 结束");
    }

    @Override
    public void onError() {
        Log.i(TAG, "onError: 出错");
    }

    @Override
    public void onPreview(List<String> selectAudio) {
        Log.i(TAG, "onPreview: 预览");
    }
}
  new FilePickerConfig.Builder()
          .iHandlerCallBack(new HandlerCallBack())
          ...//其他图标及属性
          .build();
  //使用UI时-修改文件图标
  new FilePickerConfig.Builder()
          .iconResources(MediaEntity.MEDIA_DOCUMENT, R.mipmap.document_icon)
          .iconResources(MediaEntity.MEDIA_AUDIO, R.mipmap.audio_icon)
          .iHandlerCallBack(new HandlerCallBack())
          ...//其他图标及属性
          .build();
  //使用UI时-修改底部功能条
  //在自己的layout文件加下新建
  //file_picker_layout_upload.xml
  //在功能条layout中添加3个TextView样式自定义id分别为
  //file_picker_tv_preview、file_picker_tv_selected、file_picker_tv_upload
```
## 混淆
-keep public class com.zhangteng.**.*{ *; }
## 历史版本
版本| 更新| 更新时间
-------- | ----- | -----
v1.0.1| 样式自定义|2020/9/8 0008 at 下午 17:58
## 赞赏

如果您喜欢FilePicker，或感觉FilePicker帮助到了您，可以点右上角“Star”支持一下，您的支持就是我的动力，谢谢

您也可以扫描下面的二维码，请作者喝杯茶 tea
![支付宝收款码](https://img-blog.csdnimg.cn/20200807160902219.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2R1b2x1bzk=,size_16,color_FFFFFF,t_70)
![微信收款码](https://img-blog.csdnimg.cn/20200807160902112.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2R1b2x1bzk=,size_16,color_FFFFFF,t_70)

## 联系我
邮箱：763263311@qq.com/ztxiaoran@foxmail.com

## License
Copyright (c) [2020] [Swing]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

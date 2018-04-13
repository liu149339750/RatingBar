# RatingBar
背景：开发的应用中需要一个评分控件展现评分数。原生的API限制太大太丑，难以满足要求，从网上也没有找到满意的开源控件。
优点：
1.无需准备图片
2.可调整星星的大小与间隔

##如何使用
```
        <com.lw.ratingbar.RatingBar xmlns:app="http://schemas.android.com/apk/res-auto"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:starBackColor="@color/default_star_backgroud_color"
            app:starCoverColor="@color/default_star_cover_color"
            app:numStars="5"
            app:rating="5"/>

注意：星星的大小靠starRadius调节，layout_width不能调整宽度，控件只会申请其本身所占用的宽度。
```
#方法与属性说明
```
app:numStars   定义五角星数目
<br />
对应方法：void setNumStar(int count)
<br />
app:maxRating  最大评分数目
对应方法：void setMaxRating(float max)
<br />
app:rating     评分，五角星被覆盖的区域比例为评分与最大评分的比例
<br />
对应方法：void setRating(float score)
<br />
app：starRadius 星星的半径，此值决定了星星的大小。
<br />
对应方法：void setStarRadius(int radius)
<br />
app：starPadding 星星间的间距
<br />
对应方法：setStarPadding(int padding)
<br />
app:starBackColor 星星的背景颜色
<br />
app:starCoverColor 遮盖星星部分颜色

app:isIndicator 是否只是个指示器

```

<img src="https://github.com/liu149339750/RatingBar/blob/master/shutcut.png"/>


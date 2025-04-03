package com.verkada.android.catpictures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.verkada.android.catpictures.data.Picture
import com.verkada.android.catpictures.network.PictureService
import com.verkada.android.catpictures.theme.CatPicturesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var catlist by remember { mutableStateOf(listOf<Picture>()) }
            LaunchedEffect(Unit) {
                catlist = showCatPics()
            }
            System.out.println("cat list size " + catlist.size)
            DisplayCatList(catlist)
            CatPicturesTheme {
            }
        }
    }

    private suspend fun showCatPics(): List<Picture> {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(PictureService.ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitobj = retrofitBuilder.create(PictureService::class.java)

        val catList: List<Picture> = retrofitobj.pictures()

        System.out.println("size of the cat list in showcatpics()" + catList.size)
        return catList
    }

    @Composable
    fun DisplayCatList(list: List<Picture>?) {
        if (list != null) {
            System.out.println("size of the cat list in DisplayCatList() " + list.size)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .height(400.dp)  // Adjust height for better scroll experience
                    .padding(8.dp)
                    .fillMaxSize()
            ) {
                items(list.size) { index ->
                   // Text(text = list.get(index).url)
                  /*  AsyncImage(
                        model = ImageRequest.Builder(baseContext)
                            .data(list.get(index).url)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )*/

                    AsyncImage(model = list.get(index).url,
                        contentDescription = null)
                }
            }
        }
    }
}

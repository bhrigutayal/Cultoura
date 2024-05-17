package com.example.tourismapp.screens

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.tourismapp.R
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.tourismapp.MainActivity
import com.example.tourismapp.Screens
import com.example.tourismapp.data.*
import com.example.tourismapp.utils.LocationUtils
import com.example.tourismapp.viewmodel.HomeViewModel
import com.example.tourismapp.viewmodel.ItineraryViewModel
import com.example.tourismapp.viewmodel.LocationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(
    homeViewModel: HomeViewModel,
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    navController: NavController,
    itineraryViewModel: ItineraryViewModel
){


    BackHandler(enabled = true) {

    }
    val scope: CoroutineScope = rememberCoroutineScope()
    val selected by homeViewModel.selected.observeAsState()
    val state by homeViewModel.state.observeAsState()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = {  val navigationIcon : (@Composable () -> Unit) =
            {
                androidx.compose.material3.IconButton(onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }



            androidx.compose.material.TopAppBar(
                title = {
                    Text(
                        text = "${selected}",
                        color = colorResource(id = R.color.white),
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .heightIn(max = 24.dp)
                    )
                },
                elevation = 3.dp,
                backgroundColor = colorResource(id = R.color.text_field),
                navigationIcon = navigationIcon
            )
        },
//        drawerContent = {
//            LazyColumn(Modifier.padding(16.dp)){
//                items(screensInDrawer){
//                        item ->
//                    DrawerItem(selected = currentRoute == item.dRoute, item = item) {
//                        scope.launch {
//                            scaffoldState.drawerState.close()
//                        }
//
//                            controller.navigate(item.dRoute)
//                            title.value = item.dTitle
//
//                    }
//                }
//            }
//        },
        bottomBar = {
            androidx.compose.material3.BottomAppBar(
                containerColor =  colorResource(id = R.color.text_field),
                ) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween) {
                    IconButton(
                        onClick = {
                            homeViewModel.Navigate("Home")

                        }, modifier = if (selected == "Home") Modifier
                            .size(50.dp)
                            .background(color = Color.White)
                            .clip(
                                CircleShape
                            )
                            .border(1.dp, Color.White) else Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = if (selected == "Home") colorResource(id = R.color.text_field) else Color.White
                        )

                    }

                    IconButton(onClick = {
                        homeViewModel.Navigate("Search")
                    }, modifier = if (selected == "Search") Modifier
                        .size(50.dp)
                        .background(color = Color.White)
                        .clip(
                            CircleShape
                        )
                        .border(1.dp, Color.White) else Modifier) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = if (selected == "Search") colorResource(id = R.color.text_field) else Color.White
                        )

                    }

                        FloatingActionButton(onClick = {
                                                      homeViewModel.Navigate( "Itinerary")
                                                       }, ) {
                            Icon(imageVector = Icons.Default.Add , contentDescription = null, tint = Color.White)
                        }


                    IconButton(onClick = {
                        homeViewModel.Navigate("Location")
                    }, modifier = if (selected == "Location") Modifier
                        .size(50.dp)
                        .background(color = Color.White)
                        .clip(
                            CircleShape
                        )
                        .border(1.dp, Color.White) else Modifier) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = if (selected == "Location") colorResource(id = R.color.text_field) else Color.White
                        )

                    }

                    IconButton(onClick = {
                        homeViewModel.Navigate("Profile")
                    }, modifier = if (selected == "Profile") Modifier
                        .size(50.dp)
                        .background(color = Color.White)
                        .clip(
                            CircleShape
                        )
                        .border(1.dp, Color.White) else Modifier) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = if (selected == "Profile") colorResource(id = R.color.text_field) else Color.White
                        )

                    }
                }
            }} ,
        scaffoldState = scaffoldState
    )
    {

        when(selected){
            "Home"-> HomeView(it,homeViewModel)
            "Search"-> SearchPage(it)
            "Location"-> LocationPage(it, state)
            "Profile" ->ProfilePage(it, homeViewModel = homeViewModel,navController = navController)
            "Itinerary" -> Itinerary(locationUtils = locationUtils, viewModel = viewModel, navController = navController, it, homeViewModel,itineraryViewModel)
            }
        }

    }



@Composable
fun StateRowItem(state:States, homeViewModel: HomeViewModel){
    Column(
        modifier= Modifier
            .size(150.dp)
            .clickable {
                homeViewModel.Navigate("Location")
                homeViewModel.setState(state.name)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = rememberAsyncImagePainter(state.image) , contentDescription = null,modifier = Modifier
            .size(100.dp)
            .clip(
                CircleShape
            )
            .border(2.dp, state.borderColor, CircleShape))
        Text(text = state.name, color = Color.Black)
    }
}

@Composable
fun ListItems(heading:String, list: MutableList<Images> = mutableListOf()){

    Column{
        Text(text = heading, color = Color.Black,fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal, fontFamily = FontFamily.SansSerif, fontSize = 25.sp)
        // This list has to be updated with real data
        if(list.size == 0) {
            list.add(Images())
            list.add(Images("https://firebasestorage.googleapis.com/v0/b/tourism-3aef0.appspot.com/o/images%2Fimages.jpg?alt=media&token=c88298e1-5754-4f64-9ca8-e2131aad8ee8"))
            list.add(Images("https://firebasestorage.googleapis.com/v0/b/tourism-3aef0.appspot.com/o/images%2Fimages%20(1).jpg?alt=media&token=d7d4c7f0-dbe7-4855-9cef-7aa3ba0321fc"))
            list.add(Images("https://firebasestorage.googleapis.com/v0/b/tourism-3aef0.appspot.com/o/images%2Fimages%20(2).jpg?alt=media&token=c8142c7a-945f-47b5-be53-659080932261"))
            list.add(Images("https://firebasestorage.googleapis.com/v0/b/tourism-3aef0.appspot.com/o/images%2Fimages%20(3).jpg?alt=media&token=3d5903ab-bd05-4e3c-8b7b-dc68568ceb48"))
        }

        LazyRow( modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            items(list){
                   image->
                ImagesWidget(image = image)

            }
        }

    }
}
@Composable
fun ImagesWidget(image: Images){
    Image(painter = rememberAsyncImagePainter(image.image) , contentDescription = null,modifier = Modifier
        .padding(15.dp)
        .size(150.dp)
        .clip(RoundedCornerShape(50.dp))
        .clickable { /*Add functionality later*/ }

    )
}
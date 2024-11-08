import com.example.quizcue.presentation.home_screen.HomeScreenViewModel

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarViewWeek
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import coil3.compose.rememberAsyncImagePainter
import com.example.quizcue.R
import com.example.quizcue.presentation.elements.CourseCard
import com.example.quizcue.presentation.tools.Screen
import dagger.hilt.android.lifecycle.HiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeScreenViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(Transparent),
                navigationIcon = {
                    IconButton(onClick = {
                        homeViewModel.logout()
                        navController.navigate(Screen.Login.route) { popUpTo(0) }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
            )
        },
        modifier = Modifier
            .padding(
                top = WindowInsets.statusBars
                    .asPaddingValues()
                    .calculateTopPadding()
            )
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        MainPreview(navController)
    }
}

@Composable
fun MainPreview(
    navController: NavController
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 30.dp)) {
        Header()
        Body()
        CourseCard(
            navController = navController,
            textColor = MaterialTheme.colorScheme.onPrimary,
            trackColor = MaterialTheme.colorScheme.tertiary,
            cardColor = CardDefaults.cardColors(MaterialTheme.colorScheme.primary))
    }
}

@Composable
fun Header(modifier: Modifier = Modifier,
        homeViewModel: HomeScreenViewModel = hiltViewModel())
{
    val userName by homeViewModel.userName.collectAsState(initial = "")
    val userEmail by homeViewModel.userEmail.collectAsState(initial = "")
    val userPhoto by homeViewModel.userPhoto.collectAsState(null)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.35f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .padding(top = 15.dp)
                .size(128.dp)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                    CircleShape
                )
                .clip(CircleShape),
            painter = if (userPhoto != null)
                BitmapPainter(userPhoto!!.asImageBitmap())
            else
                painterResource(id = R.drawable.koshka),
            contentDescription = "user",
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0.5f) })
        )
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = userEmail,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun Body() {
    val activeItems = listOf(
        listOf(Icons.Rounded.CalendarToday, "Сегодня", "6 повторений"),
        listOf(Icons.Filled.CalendarViewWeek, "На этой неделе", "20 повторений"),
        listOf(Icons.Filled.CalendarMonth, "В этом месяце", "30 повторений")
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.45f)
            .border(1.dp, MaterialTheme.colorScheme.tertiary, FloatingActionButtonDefaults.shape)
            .padding(20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            activeItems.forEach { item ->
                ActiveItem(item[0] as ImageVector, item[1].toString(), item[2].toString())
            }
        }
    }
}

@Composable
fun ActiveItem(icon: ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        FloatingActionButton(
            modifier = Modifier.border(
                1.dp,
                MaterialTheme.colorScheme.tertiary,
                FloatingActionButtonDefaults.shape
            ),
            onClick = { /* TODO */ },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(imageVector = icon, contentDescription = null)
        }
        Spacer(modifier = Modifier.fillMaxWidth(0.25f))
        Column {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = subtitle, style = MaterialTheme.typography.titleSmall)
        }
    }
}

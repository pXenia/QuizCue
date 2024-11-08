package com.example.quizcue.presentation.authentication.register_screen

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import coil3.compose.rememberAsyncImagePainter
import com.example.quizcue.R
import com.example.quizcue.domain.Response
import com.example.quizcue.presentation.tools.CircularProgress
import com.example.quizcue.presentation.tools.Screen
import com.google.android.play.integrity.internal.s
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterScreenViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val hostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = hostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "Register") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Content(
            paddingValues = paddingValues,
            registerFlowState = registerViewModel.registerFlow,
            onNavigateToLogin = { navController.popBackStack() },
            onRegister = { email, password, name, image -> registerViewModel.register(email, password, name, image) },
            registerSuccess = { navController.navigate(Screen.Home.route) },
            registerError = { scope.launch { hostState.showSnackbar("Упс! что-то пошло не так, " +
                    "проверьте подключение и повторите попытку") } }
        )
    }
}

@Composable
fun Content(
    paddingValues: PaddingValues,
    registerFlowState: MutableSharedFlow<Response<AuthResult>>,
    onRegister: (String, String, String, Uri?) -> Unit,
    onNavigateToLogin: () -> Unit,
    registerSuccess: () -> Unit,
    registerError: () -> Unit
) {
    val nameText = remember {
        mutableStateOf("")
    }
    val emailText = remember {
        mutableStateOf("")
    }
    val passwordText = remember {
        mutableStateOf("")
    }
    val selectedImageUri = remember{ mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri.value = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(top = 15.dp)
                .size(150.dp)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                    CircleShape
                )
                .clip(CircleShape)
                .clickable{launcher.launch("image/*")},
            painter = if (selectedImageUri.value != null)
                rememberAsyncImagePainter(selectedImageUri.value)
            else
                painterResource(id = R.drawable.koshka),
            contentDescription = "user",
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0.5f) })
        )
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = "Добавить фото",
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .align(alignment = Alignment.CenterHorizontally)
                .padding(20.dp)
                .clickable { },
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 20.dp, end = 20.dp, top = 5.dp),
            value = nameText.value,
            onValueChange = { text -> nameText.value = text },
            label = { Text("Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            leadingIcon = { Icon(Icons.Filled.AccountCircle, "email") },
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp),
            value = emailText.value,
            onValueChange = { text -> emailText.value = text },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = { Icon(Icons.Filled.Email, "email") },
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            value = passwordText.value,
            onValueChange = { text -> passwordText.value = text },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            leadingIcon = { Icon(Icons.Filled.Lock, "password") },
        )
        Button(
            onClick = { onRegister(emailText.value, passwordText.value, nameText.value, selectedImageUri.value
            ) },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp),
            content = { Text(text = "Register") }
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = buildAnnotatedString {
                append("Already have an account?")
                withStyle(style = SpanStyle(MaterialTheme.colorScheme.primary)) { append(" Login") }
            },
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .align(alignment = Alignment.CenterHorizontally)
                .padding(20.dp)
                .clickable { onNavigateToLogin() }
        )
    }
    RegisterState(
        registerFlowState = registerFlowState,
        onSuccess = { registerSuccess() },
        onError = { registerError() }
    )
}

@Composable
fun RegisterState(
    registerFlowState: MutableSharedFlow<Response<AuthResult>>,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    if (isLoading.value) CircularProgress()
    LaunchedEffect(Unit) {
        registerFlowState.collect {
            when (it) {
                is Response.Loading -> isLoading.value = true

                is Response.Error -> {
                    isLoading.value = false
                    onError()
                }

                is Response.Success -> {
                    isLoading.value = false
                    onSuccess()
                }
            }
        }
    }
}
package com.jorgecastanov.mercadolibrecandidate.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.TopAppBar
import androidx.compose.material.Text
import androidx.compose.material.IconButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import com.jorgecastanov.mercadolibrecandidate.R
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun NavigationAppBar(
    title: String = "",
    navigationIcon: ImageVector? = null,
    navigationAction: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (navigationIcon != null) {
                IconButton(onClick = {
                    navigationAction()
                }) {
                    Icon(navigationIcon, "")
                }
            }
        },
        backgroundColor = MaterialTheme.colors.primary
    )
}

@Composable
fun ActionAppBar(
    title: String = "",
    actionIcon: ImageVector? = null,
    action: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(title) },
        actions = {
            if (actionIcon != null) {
                IconButton(onClick = {
                    action()
                }) {
                    Icon(actionIcon, "")
                }
            }
        },
        backgroundColor = MaterialTheme.colors.primary
    )
}

@Composable
fun SearchAppBar(
    text: String = "",
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.primary
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = text,
            onValueChange = { value -> onTextChange(value) },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    text = stringResource(R.string.search),
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier.alpha(ContentAlpha.medium),
                    onClick = {
                        focusManager.clearFocus()
                        onSearchClicked(text)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(
                    alpha = ContentAlpha.medium
                )
            )
        )
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun FeedAppBar(title: String = "",onSearchClicked: (String) -> Unit = {}) {
    var searchState by remember { mutableStateOf(SearchState.CLOSED) }
    var searchText by remember { mutableStateOf("") }

    when (searchState) {
        SearchState.CLOSED -> {
            ActionAppBar(
                title = title,
                actionIcon = Icons.Default.Search,
                action = {
                    searchState = SearchState.OPENED
                }
            )
        }
        SearchState.OPENED -> {
            SearchAppBar(
                text = searchText,
                onTextChange = { value ->
                    searchText = value
                },
                onCloseClicked = {
                    searchState = SearchState.CLOSED
                },
                onSearchClicked = {
                    onSearchClicked(searchText)
                }
            )
        }
    }
}

enum class SearchState {
    OPENED,
    CLOSED
}

package com.jorgecastanov.mercadolibrecandidate.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jorgecastanov.mercadolibrecandidate.ui.theme.MercadoLibreCandidateTheme

typealias OnTab = () -> Unit

@Composable
fun ProductCard(
    title: String,
    price: Int,
    available_quantity: Int,
    condition: String,
    permalink: String,
    thumbnail: String,
    selected: OnTab
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { selected() }
            .size(300.dp),
        elevation = 10.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.h4
                )
                Text(
                    "$$price",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    "$available_quantity",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    condition,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    permalink,
                    style = MaterialTheme.typography.body1
                )
                AsyncImage(
                    model = thumbnail,
                    contentDescription = ""
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    MercadoLibreCandidateTheme {
        ProductCard(
            title = "Samsung Galaxy J4+ Dual Sim 32 Gb Negro (2 Gb Ram)",
            price = 19609,
            condition = "new",
            available_quantity = 1,
            permalink = "https://www.mercadolibre.com.ar/p/MLA13550363",
            thumbnail = "http://mla-s1-p.mlstatic.com/943469-MLA31002769183_062019-I.jpg",
        ) {}
    }
}
package com.tunnexa.android.ui.views.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.io.path.Path
import kotlin.io.path.moveTo

@Composable
fun FeedbackPopup(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .wrapContentSize(Alignment.TopCenter)
            .padding(top = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Pointer triangle (speech bubble tail)
            Canvas(
                modifier = Modifier
                    .size(20.dp, 10.dp)
            ) {
                val path = Path().apply {
                    moveTo(size.width / 2, 0f)
                    lineTo(0f, size.height)
                    lineTo(size.width, size.height)
                    close()
                }
                drawPath(path, color = Color(0xFF192655))
            }

            // Main card
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFF141E42), Color(0xFF1E2A68))
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .shadow(8.dp, RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text = "feedback & opinions",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(16.dp))

                Text("Email:", color = Color.White, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Enter your email", color = Color.Gray) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = OutlinedTextFieldDefaults.colors()
//                        TextFieldColors(
//                        focusedBorderColor = Color(0xFF2E3A75),
//                        unfocusedBorderColor = Color(0xFF2E3A75),
//                        textColor = Color.White,
//                        cursorColor = Color.White
//                    )
                )

                Spacer(Modifier.height(12.dp))

                Text("Your Feedback:", color = Color.White, fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = feedback,
                    onValueChange = { feedback = it },
                    placeholder = { Text("Write your message here", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    colors = OutlinedTextFieldDefaults.colors()
//                        TextFieldDefaults.outlinedTextFieldColors(
//                        focusedBorderColor = Color(0xFF2E3A75),
//                        unfocusedBorderColor = Color(0xFF2E3A75),
//                        textColor = Color.White,
//                        cursorColor = Color.White
//                    )
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Ignore", color = Color.White)
                    }

                    Button(
                        onClick = { onSubmit(email, feedback) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text("Submit", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
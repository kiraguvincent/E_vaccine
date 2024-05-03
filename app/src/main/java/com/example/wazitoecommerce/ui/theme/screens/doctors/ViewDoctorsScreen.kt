
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.wazitoecommerce.data.DoctorViewModel
import com.example.wazitoecommerce.data.ProductViewModel
import com.example.wazitoecommerce.models.Doctor
import com.example.wazitoecommerce.models.Product
import com.example.wazitoecommerce.ui.theme.WazitoECommerceTheme

@Composable
fun ViewDoctorsScreen(navController:NavHostController) {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        var context = LocalContext.current
        var doctorRepository = DoctorViewModel(navController, context)


        val emptyDoctorState = remember { mutableStateOf(Doctor("","","","","")) }
        var emptyDoctorsListState = remember { mutableStateListOf<Doctor>() }

        var doctors =doctorRepository.allDoctors(emptyDoctorState, emptyDoctorsListState)


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "All Doctors",
                fontSize = 30.sp,
                fontFamily = FontFamily.Cursive,
                color = Color.Red)

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(){
                items(doctors){
                    DoctorItem(
                        name = it.name,
                        specialisation= it.specialisation,
                        age = it.age,
                        id = it.id,
                        navController = navController,
                        doctorRepository = doctorRepository,
                        doctorImage = it.imageUrl
                    )
                }
            }
        }
    }
}


@Composable
fun DoctorItem(name:String,specialisation :String, age:String, id:String,
               navController:NavHostController,
               doctorRepository:DoctorViewModel, doctorImage:String) {

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = name)
        Text(text = specialisation)
        Text(text =age )
        Image(
            painter = rememberAsyncImagePainter(doctorImage),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )
        Button(onClick = {
            doctorRepository.deleteDoctor(id)
        }) {
            Text(text = "Delete")
        }
        Button(onClick = {
            //navController.navigate(ROUTE_UPDATE_PRODUCTS+"/$id")
        }) {
            Text(text = "Update")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ViewDoctorsScreenPreview(){
    WazitoECommerceTheme {
        ViewDoctorsScreen(navController = rememberNavController())
    }
}

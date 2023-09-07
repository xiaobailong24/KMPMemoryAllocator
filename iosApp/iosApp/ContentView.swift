import UIKit
import SwiftUI
import shared

struct ContentView: View {
    @State private var count = 0

    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundColor(.accentColor)
            Text("Hello, world!")
            Text("\(count)")
                .onTapGesture {
                    self.count += 1
                    IosInteropTest.shared.test()
                }

        }
        .padding()
    }

}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}




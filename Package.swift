// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CrepePlugin",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CrepePlugin",
            targets: ["CrepePlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "CrepePlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/CrepePlugin"),
        .testTarget(
            name: "CrepePluginTests",
            dependencies: ["CrepePlugin"],
            path: "ios/Tests/CrepePluginTests")
    ]
)
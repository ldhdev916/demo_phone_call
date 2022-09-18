import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

@pragma("vm:entry-point")
void overlayMain(List<String> args) {
  final phone = args[0];

  runApp(MaterialApp(
    debugShowCheckedModeBanner: false,
    home: Padding(
      padding: const EdgeInsets.symmetric(horizontal: 20),
      child: Material(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 20),
          child: Column(
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  IconButton(onPressed: () {}, icon: const Icon(Icons.home)),
                  IconButton(onPressed: () {}, icon: const Icon(Icons.close)),
                ],
              ),
              const Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  "후후앤컴퍼니",
                  style: TextStyle(fontSize: 24, fontWeight: FontWeight.w600),
                ),
              ),
              Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  phone,
                  style: const TextStyle(fontWeight: FontWeight.w600),
                ),
              ),
              Align(
                alignment: Alignment.centerRight,
                child: TextButton(
                    onPressed: () {},
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: const [
                        Icon(
                          Icons.arrow_back_ios_new,
                          size: 20,
                          color: Colors.black,
                        ),
                        Text(
                          "바로 차단",
                          style: TextStyle(color: Colors.black),
                        )
                      ],
                    )),
              )
            ],
          ),
        ),
      ),
    ),
  ));
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: Scaffold(),
    );
  }
}

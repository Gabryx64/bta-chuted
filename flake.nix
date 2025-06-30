{
    description = "A Nix-flake-based Scala and Java development environment";

    inputs.nixpkgs.url = "https://flakehub.com/f/NixOS/nixpkgs/0.1.*.tar.gz";

    outputs = { self, nixpkgs }:
        let
            javaVersion = 17; # Change this value to update the whole stack

            supportedSystems = [ "x86_64-linux" "aarch64-linux" "x86_64-darwin" "aarch64-darwin" ];
            forEachSupportedSystem = f: nixpkgs.lib.genAttrs supportedSystems (system: f {
                pkgs = import nixpkgs { inherit system; overlays = [ self.overlays.default ]; };
            });
        in
            {
            overlays.default = final: prev:
                let
                    jdk = prev."jdk${toString javaVersion}";
                in
                    {
                    sbt = prev.sbt.override { jre = jdk; };
                    scala = prev.scala_3.override { jre = jdk; };
                    java = jdk;
                };

            devShells = forEachSupportedSystem ({ pkgs }: {
                default = pkgs.mkShell {
                    packages = with pkgs; [
                        scala
                        sbt
                        coursier
                        java
                        gradle
                        maven

                        jdt-language-server
                        metals

                        xorg.libXxf86vm
                        xorg.libX11
                        libGL
                        mesa
                        openalSoft
                    ];
                    shellHook = ''
                        export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${pkgs.xorg.libXxf86vm.out}/lib:${pkgs.xorg.libX11.out}/lib:${pkgs.libGL.out}/lib:${pkgs.mesa.out}/lib
                        export JAVA_TOOL_OPTIONS="-Dorg.lwjgl.openal.libname=${pkgs.openalSoft.out}/lib/libopenal.so"
                    '';
                };
            });
        };
}
